package ru.taynov.share.service

import java.io.InputStream
import java.time.ZonedDateTime
import java.util.UUID
import org.springframework.web.multipart.MultipartFile

interface StorageService {

    fun saveFile(file: MultipartFile, id: UUID?)

    fun getFile(id: UUID): InputStream

    fun deleteFile(id: UUID)

    fun getFileUploadDate(id: UUID): ZonedDateTime
}