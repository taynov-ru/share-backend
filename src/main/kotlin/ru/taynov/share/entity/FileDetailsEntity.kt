package ru.taynov.share.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "file_details")
data class FileDetailsEntity (
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    val fileId: UUID? = null,
    @Column(name = "downloads_limit")
    val downloadsLimit: Int,
    @Column(name = "downloads_count")
    val downloadsCount: Int = 0,
    @Column(name = "expiration_time")
    val expirationTime: Long,
    @Column(name = "upload_date")
    val uploadDate: ZonedDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    var publication: PublicationEntity? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "file_id")
    val uploadedFileId: FileEntity? = null
)