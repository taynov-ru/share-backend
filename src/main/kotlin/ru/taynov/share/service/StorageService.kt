package ru.taynov.share.service

import java.io.InputStream
import java.util.*
import org.springframework.web.multipart.MultipartFile

interface StorageService {

    fun saveFile(file: MultipartFile, id: UUID?)

    fun getInputStream(id: UUID): InputStream
}