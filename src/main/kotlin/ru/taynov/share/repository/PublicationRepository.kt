package ru.taynov.share.repository

import java.time.ZonedDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taynov.share.entity.PublicationEntity

@Repository
interface PublicationRepository: JpaRepository<PublicationEntity, Long> {

    fun findById(id: UUID): PublicationEntity?

    fun findByDownloadLink(downloadLink: String): PublicationEntity?

    fun findByExpirationDateBeforeAndDeletedFalse(date: ZonedDateTime): List<PublicationEntity>?

    fun existsByDownloadLink(link: String): Boolean?
}