package ru.taynov.share.service

import java.util.*

interface ValidationService {

    fun validateFileSize(fileSize: Long): Boolean

    fun validateFileIds(fileIds: List<UUID>): Boolean

    fun validateFileIdsSize(size: Int): Boolean

    fun validatePassword(publicationPassword: String?, enteredPassword: String?): Boolean
}