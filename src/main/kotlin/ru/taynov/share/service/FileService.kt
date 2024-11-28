package ru.taynov.share.service

import org.springframework.web.multipart.MultipartFile
import ru.taynov.openapi.model.FilePublishRequestGen
import ru.taynov.openapi.model.FilePublishResponseDataGen
import ru.taynov.openapi.model.GetPublicationResponseDataGen
import ru.taynov.share.dto.UploadedFileResponse
import java.util.*

interface FileService {

    fun uploadFile(file: MultipartFile): UploadedFileResponse

    fun deleteFile(id: UUID)

    fun publishFile(filePublishRequest: FilePublishRequestGen): FilePublishResponseDataGen

    fun deletePublication(id: UUID)

    fun getPublication(downloadLink: String, password: String?): GetPublicationResponseDataGen

    fun getFileUrl(id: UUID, password: String?): String
}