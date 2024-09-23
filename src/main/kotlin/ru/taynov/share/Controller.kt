package ru.taynov.share

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.taynov.swagger.client.DefaultApi
import ru.taynov.swagger.model.FileDownloadResponseDataGen
import ru.taynov.swagger.model.FilePublishRequestGen
import ru.taynov.swagger.model.FilePublishResponseDataGen
import ru.taynov.swagger.model.UploadedFileGen

@RestController
class Controller: DefaultApi {
    override fun downloadFile(): ResponseEntity<FileDownloadResponseDataGen> {
        TODO("Not yet implemented")
    }

    override fun publishFile(body: FilePublishRequestGen?): ResponseEntity<FilePublishResponseDataGen> {
        TODO("Not yet implemented")
    }

    override fun uploadFile(file: MultipartFile?): ResponseEntity<UploadedFileGen> {
        TODO("Not yet implemented")
    }
}