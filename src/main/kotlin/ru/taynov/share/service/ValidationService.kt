package ru.taynov.share.service

import java.util.UUID

interface ValidationService {

    fun validateFileSize(fileSize: Long)

    fun validatePassword(publicationPassword: String?, enteredPassword: String?)

    fun validatePublishFiles(fileIds: List<UUID>, size: Int, link: String)
}