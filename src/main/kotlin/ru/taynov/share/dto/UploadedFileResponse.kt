package ru.taynov.share.dto

import java.util.*

data class UploadedFileResponse(
    val fileId: UUID?,
    val fileName: String
)
