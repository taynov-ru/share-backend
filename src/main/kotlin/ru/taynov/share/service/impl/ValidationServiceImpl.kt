package ru.taynov.share.service.impl

import java.util.*
import org.springframework.stereotype.Service
import ru.taynov.share.service.ValidationService
import ru.taynov.share.utils.MAX_FILES_NUMBER
import ru.taynov.share.utils.MAX_FILE_SIZE

@Service
class ValidationServiceImpl: ValidationService {
    override fun validateFileSize(fileSize: Long): Boolean {
        return fileSize > MAX_FILE_SIZE
    }

    override fun validateFileIds(fileIds: List<UUID>): Boolean {
        return fileIds.isEmpty()
    }

    override fun validateFileIdsSize(size: Int): Boolean {
        return size > MAX_FILES_NUMBER
    }

    override fun validatePassword(publicationPassword: String?, enteredPassword: String?): Boolean {
        return !publicationPassword.isNullOrEmpty() && (publicationPassword != enteredPassword)
    }
}