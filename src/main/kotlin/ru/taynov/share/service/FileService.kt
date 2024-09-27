package ru.taynov.share.service

import java.util.*
import org.springframework.web.multipart.MultipartFile
import ru.taynov.share.dto.UploadedFileResponse

interface FileService {
    fun uploadFile(file: MultipartFile): UploadedFileResponse

    fun deleteFile(id: UUID)
}