package ru.taynov.share.entity

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
    val id: UUID,
    @Column(name = "publish_date")
    val publishDate: LocalDateTime,
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime,
    @Column(name = "deleted")
    val deleted: Boolean = false,

    @OneToMany(mappedBy = "publication", fetch = FetchType.LAZY)
    val files: List<FileEntity>
)