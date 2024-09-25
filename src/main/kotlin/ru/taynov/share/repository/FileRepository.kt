package ru.taynov.share.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taynov.share.entity.FileEntity

@Repository
interface FileRepository: JpaRepository<FileEntity, Long> {
}