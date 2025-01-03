package ru.taynov.share.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.taynov.openapi.client.DefaultApi
import ru.taynov.openapi.model.FilePublishRequestGen
import ru.taynov.openapi.model.FilePublishResponseDataGen
import ru.taynov.openapi.model.GetPublicationResponseDataGen
import ru.taynov.share.dto.UploadedFileResponse
import ru.taynov.share.service.FileService
import java.util.*

@RestController
@RequestMapping("/api/v1")
class FileController(
    private val fileService: FileService
) : DefaultApi {

    @PostMapping("/files/upload")
    fun uploadFile(@RequestParam file: MultipartFile): ResponseEntity<UploadedFileResponse> {
        return ResponseEntity.ok(fileService.uploadFile(file))
    }

    override fun deleteFile(id: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(fileService.deleteFile(id))
    }

    override fun publishFile(body: FilePublishRequestGen): ResponseEntity<FilePublishResponseDataGen> {
        return ResponseEntity.ok(fileService.publishFile(body))
    }

    override fun deletePublication(id: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(fileService.deletePublication(id))
    }

    override fun getPublication(downloadLink: String, password: String?):
            ResponseEntity<GetPublicationResponseDataGen> {
        return ResponseEntity.ok(fileService.getPublication(downloadLink, password))
    }

    @GetMapping("/file/url")
    fun getFileUrl(
        @RequestParam id: UUID, @RequestHeader(required = false) password: String?
    ): ResponseEntity<String> {
        return ResponseEntity.ok().body(fileService.getFileUrl(id, password))
    }
}