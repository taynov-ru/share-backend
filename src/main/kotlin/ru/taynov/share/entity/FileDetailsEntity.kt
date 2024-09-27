package ru.taynov.share.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "file_details")
data class FileDetailsEntity (
    @Id
    @Column(name = "file_id")
    val fileId: UUID,
    @Column(name = "downloads_limit")
    val downloadsLimit: Int,
    @Column(name = "downloads_count")
    val downloadsCount: Int,
    @Column(name = "expiration_time")
    val expirationTime: Long,
    @Column(name = "password")
    val password: String,
    @Column(name = "link")
    val link: String,

    @OneToOne
    @JoinColumn(name = "file_id", referencedColumnName = "id", insertable = false, updatable = false)
    val uploadedFileId: FileEntity
)