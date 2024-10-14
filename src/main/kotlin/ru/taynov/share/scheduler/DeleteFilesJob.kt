package ru.taynov.share.scheduler

import jakarta.transaction.Transactional
import java.time.ZonedDateTime
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.taynov.share.repository.FileDetailsRepository
import ru.taynov.share.repository.FileRepository
import ru.taynov.share.repository.PublicationRepository
import ru.taynov.share.service.StorageService

open class DeleteFilesJob(
    private val fileRepository: FileRepository,
    private val fileDetailsRepository: FileDetailsRepository,
    private val storageService: StorageService,
    private val publicationRepository: PublicationRepository
): Job {

    @Transactional
    override fun execute(p0: JobExecutionContext?) {
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

            val unrelatedFilesToDelete = fileDetailsRepository.findAllByPublicationIsNull()
                ?.filter { details -> details.uploadDate.plusHours(12).isBefore(now)
                        && details.uploadedFileId?.deleted == false }
            unrelatedFilesToDelete?.forEach { fileDetails ->
                fileDetails.uploadedFileId?.let { file ->
                    log.info("Marking file as deleted: ${file.fileName}")
                    file.fileUuid?.let { storageService.deleteFile(it) }
                    fileRepository.save(file.copy(deleted = true))
                }
            }

        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw JobExecutionException()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(DeleteFilesJob::class.java)
    }
}