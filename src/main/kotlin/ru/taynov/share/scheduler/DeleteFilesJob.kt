package ru.taynov.share.scheduler

import jakarta.transaction.Transactional
import java.time.ZonedDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.taynov.share.repository.FileRepository
import ru.taynov.share.repository.PublicationRepository
import ru.taynov.share.service.StorageService

@Component
class DeleteFilesJob(
    private val fileRepository: FileRepository,
    private val storageService: StorageService,
    private val publicationRepository: PublicationRepository,
) {

    @Transactional
    fun execute() {
        runCatching {
            val now = ZonedDateTime.now()
            val expiredPublication = publicationRepository.findByExpirationDateBeforeAndDeletedFalse(now)
            expiredPublication?.forEach { pub ->
                log.info("Marking publication and its files as deleted: ${pub.id}")
                publicationRepository.save(pub.copy(deleted = true))
                pub.files.forEach { fileDetails ->
                    fileDetails.uploadedFileId?.let { file ->
                        file.fileUuid?.let { storageService.deleteFile(it) }
                        fileRepository.save(file.copy(deleted = true))
                    }
                }
            }

            val unrelatedFilesToDelete = fileRepository.findAllByFileDetailsIsNullAndDeletedIsFalse()
                ?.filter { file ->
                    file.fileUuid?.let { storageService.getFileUploadDate(it).plusMinutes(3).isBefore(now) } == true
                }
            unrelatedFilesToDelete?.forEach { file ->
                log.info("Marking file as deleted: ${file.fileName}")
                file.fileUuid?.let { storageService.deleteFile(it) }
                fileRepository.save(file.copy(deleted = true))
            }
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw RuntimeException()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(DeleteFilesJob::class.java)
    }
}