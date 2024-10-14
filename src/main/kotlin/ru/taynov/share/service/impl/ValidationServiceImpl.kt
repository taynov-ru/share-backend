package ru.taynov.share.service.impl

import java.util.UUID
import org.springframework.stereotype.Service
import ru.taynov.share.enums.FileExceptionCode.ERROR_EMPTY_FILE
import ru.taynov.share.enums.FileExceptionCode.FILE_LIMIT_EXCEEDED
import ru.taynov.share.enums.FileExceptionCode.INVALID_FILE_SIZE
import ru.taynov.share.enums.FileExceptionCode.PASSWORD_DOES_NOT_MATCH
import ru.taynov.share.enums.FileExceptionCode.LINK_ALREADY_EXISTS
import ru.taynov.share.repository.PublicationRepository
import ru.taynov.share.service.ValidationService
import ru.taynov.share.utils.MAX_FILES_NUMBER
import ru.taynov.share.utils.MAX_FILE_SIZE

@Service
class ValidationServiceImpl(
    private val publicationRepository: PublicationRepository
): ValidationService {

    override fun validateFileSize(fileSize: Long) {
        if (fileSize > MAX_FILE_SIZE) throw INVALID_FILE_SIZE.getException()
    }

    override fun validatePassword(publicationPassword: String?, enteredPassword: String?) {
        if (!publicationPassword.isNullOrEmpty() && (publicationPassword != enteredPassword))
            throw PASSWORD_DOES_NOT_MATCH.getException()
    }

    override fun validatePublishFiles(fileIds: List<UUID>, size: Int, link: String) {
        if (publicationRepository.existsByDownloadLink(link) == true) throw LINK_ALREADY_EXISTS.getException()
        if (fileIds.isEmpty()) throw ERROR_EMPTY_FILE.getException()
        if (size > MAX_FILES_NUMBER) throw FILE_LIMIT_EXCEEDED.getException()
    }
}