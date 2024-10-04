package ru.taynov.share.service

import java.util.*
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import ru.taynov.openapi.model.FilePublishRequestGen
import ru.taynov.openapi.model.FilePublishResponseDataGen
import ru.taynov.openapi.model.GetPublicationRequestGen
import ru.taynov.openapi.model.GetPublicationResponseDataGen
import ru.taynov.share.dto.DownloadFileRequest
import ru.taynov.share.dto.UploadedFileResponse

interface FileService {

    fun uploadFile(file: MultipartFile): UploadedFileResponse

    fun deleteFile(id: UUID)

    fun publishFile(filePublishRequest: FilePublishRequestGen): FilePublishResponseDataGen

    fun deletePublication(id: UUID)

    fun getPublication(id: UUID, getPublicationRequestGen: GetPublicationRequestGen?): GetPublicationResponseDataGen

    fun getFilename(id: UUID): String

    fun getFileResource(id: UUID, downloadFileRequest: DownloadFileRequest): Resource
}