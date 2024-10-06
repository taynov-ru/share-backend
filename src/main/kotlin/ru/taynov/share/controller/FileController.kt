package ru.taynov.share.controller

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
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
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

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

    override fun getPublication(id: UUID, password: String?):
            ResponseEntity<GetPublicationResponseDataGen> {
        return ResponseEntity.ok(fileService.getPublication(id, password))
    }

    @GetMapping("/files", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun downloadFile(
        @RequestParam id: UUID,
        @RequestHeader(required = false) password: String?
    ): ResponseEntity<Resource> {
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                fileService.getFilename(id), StandardCharsets.UTF_8))
            .body(fileService.getFileResource(id, password))
    }
}