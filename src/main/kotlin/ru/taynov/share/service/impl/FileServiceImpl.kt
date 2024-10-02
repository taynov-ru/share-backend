package ru.taynov.share.service.impl

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
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
import ru.taynov.share.repository.FileDetailsRepository
import ru.taynov.share.repository.FileRepository
import ru.taynov.share.repository.PublicationRepository
import ru.taynov.share.service.FileService

@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val publicationRepository: PublicationRepository,
    private val fileDetailsRepository: FileDetailsRepository,
) : FileService {

    override fun uploadFile(file: MultipartFile): UploadedFileResponse {
        val uploadedFile = fileRepository.save(
            FileEntity(
                fileName = file.originalFilename ?: "untitled",
                size = file.size
            )
        )
        return UploadedFileResponse(uploadedFile.fileUuid, uploadedFile.fileName)
    }

    override fun deleteFile(id: UUID) {
        val file = fileRepository.findByFileUuid(id)
        fileRepository.save(file.copy(deleted = true))
    }

    override fun publishFile(filePublishRequest: FilePublishRequestGen): FilePublishResponseDataGen {
        val publishDate = LocalDateTime.now()
        val publication = publicationRepository.save(
            PublicationEntity(
                publishDate = publishDate,
                expirationDate = publishDate.plus(
                    filePublishRequest.fileDetails.expirationTime.toLong(),
                    ChronoUnit.SECONDS
                ),
                downloadLink = filePublishRequest.fileDetails.link
            )
        )

        filePublishRequest.fileIds.forEach { fileId ->
            val fileEntity = fileRepository.findByFileUuid(fileId)
            fileDetailsRepository.save(
                FileDetailsEntity(
                    downloadsLimit = filePublishRequest.fileDetails.downloadLimit,
                    expirationTime = filePublishRequest.fileDetails.expirationTime.toLong(),
                    password = filePublishRequest.fileDetails.password ?: "",
                    publication = publication,
                    uploadedFileId = fileEntity
                )
            )
        }
        return FilePublishResponseDataGen(
            link = publication.downloadLink
        )
    }

    override fun deletePublication(id: UUID) {
        val publication = publicationRepository.findById(id)
        publication.files.forEach {
            val file = fileRepository.findByFileUuid(it.fileId)
            fileRepository.save(file.copy(deleted = true))
        }
        publicationRepository.save(publication.copy(deleted = true))
    }

    override fun getPublication(id: UUID): GetPublicationResponseDataGen {
        val publication = publicationRepository.findById(id)
        val downloadedFiles = publication.files.map { details ->
            val file = fileRepository.findByFileUuid(details.fileId)
            val downloadsLeft = if (details.downloadsLimit != 0) {
                 details.downloadsLimit - details.downloadsCount
            } else { null }
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
}