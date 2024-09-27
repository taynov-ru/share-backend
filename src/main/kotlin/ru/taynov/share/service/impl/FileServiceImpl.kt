package ru.taynov.share.service.impl

import java.util.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.taynov.share.dto.UploadedFileResponse
import ru.taynov.share.entity.FileEntity
import ru.taynov.share.repository.FileRepository
import ru.taynov.share.service.FileService

@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
): FileService {

    override fun uploadFile(file: MultipartFile): UploadedFileResponse {
        val uploadedFile = fileRepository.save(FileEntity(
            fileName = file.originalFilename ?: "untitled",
            size = file.size
        ))
        return UploadedFileResponse(uploadedFile.fileUuid, uploadedFile.fileName)
    }

    override fun deleteFile(id: UUID) {
        val file = fileRepository.findByFileUuid(id)
        fileRepository.save(file.copy(deleted = true))
    }
}