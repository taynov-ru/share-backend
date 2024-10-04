package ru.taynov.share.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "publications")
data class PublicationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,
    @Column(name = "publish_date")
    val publishDate: LocalDateTime,
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime,
    @Column(name = "download_link")
    val downloadLink: String,
    @Column(name = "deleted")
    val deleted: Boolean = false,
    @Column(name = "password")
    val password: String?,

    @OneToMany(mappedBy = "publication", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val files: List<FileDetailsEntity> = emptyList()
)