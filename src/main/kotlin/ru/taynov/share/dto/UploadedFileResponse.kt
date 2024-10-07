package ru.taynov.share.dto

import java.util.UUID

data class UploadedFileResponse(
    val fileId: UUID?,
    val fileName: String
)
