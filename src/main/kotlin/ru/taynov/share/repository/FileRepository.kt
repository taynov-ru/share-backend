package ru.taynov.share.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taynov.share.entity.FileEntity

@Repository
interface FileRepository: JpaRepository<FileEntity, Long> {
    fun findByFileUuid(uuid: UUID?): FileEntity?
}