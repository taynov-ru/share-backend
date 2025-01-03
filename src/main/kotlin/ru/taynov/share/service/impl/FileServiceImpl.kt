package ru.taynov.share.service.impl

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.taynov.openapi.model.DownloadedFilesGen
import ru.taynov.openapi.model.FilePublishRequestGen
import ru.taynov.openapi.model.FilePublishResponseDataGen
import ru.taynov.openapi.model.GetPublicationResponseDataGen
import ru.taynov.share.dto.UploadedFileResponse
import ru.taynov.share.entity.FileDetailsEntity
import ru.taynov.share.entity.FileEntity
import ru.taynov.share.entity.PublicationEntity
import ru.taynov.share.enums.FileExceptionCode.FILE_CANNOT_BE_DELETED
import ru.taynov.share.enums.FileExceptionCode.FILE_DOWNLOAD_LIMIT_EXCEEDED
import ru.taynov.share.enums.FileExceptionCode.FILE_NOT_FOUND
import ru.taynov.share.enums.FileExceptionCode.PUBLICATION_NOT_FOUND
import ru.taynov.share.repository.FileDetailsRepository
import ru.taynov.share.repository.FileRepository
import ru.taynov.share.repository.PublicationRepository
import ru.taynov.share.service.FileService
import ru.taynov.share.service.StorageService
import ru.taynov.share.service.ValidationService
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val publicationRepository: PublicationRepository,
    private val fileDetailsRepository: FileDetailsRepository,
    private val storageService: StorageService,
    private val validationService: ValidationService
) : FileService {

    @Transactional
    override fun uploadFile(file: MultipartFile): UploadedFileResponse {
        validationService.validateFileSize(file.size)
        val uploadedFile = fileRepository.save(
            FileEntity(
                fileName = file.originalFilename ?: "untitled",
                size = file.size
            )
        )
        storageService.saveFile(file, uploadedFile.fileUuid)
        return UploadedFileResponse(uploadedFile.fileUuid, uploadedFile.fileName)
    }

    @Transactional
    override fun deleteFile(id: UUID) {
        val file = fileRepository.findByFileUuid(id) ?: throw FILE_NOT_FOUND.getException()
        val fileDetails = fileDetailsRepository.findByFileId(id)
        if (fileDetails?.publication != null) throw FILE_CANNOT_BE_DELETED.getException()
        storageService.deleteFile(id)
        fileRepository.save(file.copy(deleted = true))
    }

    @Transactional
    override fun publishFile(filePublishRequest: FilePublishRequestGen): FilePublishResponseDataGen {
        val publishDate = ZonedDateTime.now()
        val fileIds = filePublishRequest.fileIds
        val fileDetails = filePublishRequest.fileDetails
        validationService.validatePublishFiles(fileIds, fileIds.size, fileDetails.link)

        val publishingFiles = fileIds.map { fileId ->
            fileRepository.findByFileUuid(fileId) ?: throw FILE_NOT_FOUND.getException()
        }

        val publication = publicationRepository.save(
            PublicationEntity(
                publishDate = publishDate,
                expirationDate = publishDate.plus(fileDetails.expirationTime.toLong(), ChronoUnit.SECONDS),
                downloadLink = fileDetails.link,
                password = fileDetails.password,
            )
        )

        publishingFiles.forEach {
            fileDetailsRepository.save(
                FileDetailsEntity(
                    downloadsLimit = fileDetails.downloadLimit,
                    expirationTime = fileDetails.expirationTime.toLong(),
                    publication = publication,
                    uploadedFileId = it,
                )
            )
        }
        return FilePublishResponseDataGen(
            link = publication.downloadLink
        )
    }

    @Transactional
    override fun deletePublication(id: UUID) {
        val publication = publicationRepository.findById(id) ?: throw PUBLICATION_NOT_FOUND.getException()
        publication.files.forEach { fileDetails ->
            val file = fileDetails.uploadedFileId ?: throw FILE_NOT_FOUND.getException()
            file.fileUuid?.let { storageService.deleteFile(it) }
            fileRepository.save(file.copy(deleted = true))
        }
        publicationRepository.save(publication.copy(deleted = true))
    }

    @Transactional
    override fun getPublication(downloadLink: String, password: String?): GetPublicationResponseDataGen {
        val publication = publicationRepository.findByDownloadLink(downloadLink)
            ?.takeIf { !it.deleted }
            ?: throw PUBLICATION_NOT_FOUND.getException()
        validationService.validatePassword(publication.password, password)
        val downloadedFiles = publication.files.map { details ->
            val file = details.uploadedFileId ?: throw FILE_NOT_FOUND.getException()
            val downloadsLeft = if (details.downloadsLimit != 0) {
                details.downloadsLimit - details.downloadsCount
            } else {
                null
            }
            DownloadedFilesGen(
                id = file.fileUuid,
                fileName = file.fileName,
                fileSize = file.size.toBigDecimal(),
                downloadsCount = details.downloadsCount,
                downloadsLeft = downloadsLeft
            )
        }
        return GetPublicationResponseDataGen(
            link = publication.downloadLink,
            uploadDate = publication.publishDate.toString(),
            expirationDate = publication.expirationDate.toString(),
            downloadedFiles = downloadedFiles
        )
    }

    @Transactional
    override fun getFileUrl(id: UUID, password: String?): String {
        val fileDetails = fileDetailsRepository.findByFileId(id) ?: throw FILE_NOT_FOUND.getException()
        if (fileDetails.publication != null) {
            validationService.validatePassword(fileDetails.publication?.password, password)
        }
        if (fileDetails.downloadsLimit != 0 && fileDetails.downloadsCount >= fileDetails.downloadsLimit) {
            throw FILE_DOWNLOAD_LIMIT_EXCEEDED.getException()
        }

        fileDetailsRepository.save(fileDetails.copy(downloadsCount = fileDetails.downloadsCount + 1))

        val filename = fileRepository.findByFileUuid(id)?.fileName ?: throw FILE_NOT_FOUND.getException()
        return storageService.getFileUrl(id, filename) ?: throw FILE_NOT_FOUND.getException()
    }
}