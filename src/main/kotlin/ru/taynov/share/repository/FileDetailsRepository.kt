package ru.taynov.share.repository

import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taynov.share.entity.FileDetailsEntity

@Repository
interface FileDetailsRepository: JpaRepository<FileDetailsEntity, Long> {
    fun findByFileId(fileId: UUID): FileDetailsEntity?
}