package ru.taynov.share.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "files")
data class FileEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_uuid", unique = true)
    val fileUuid: UUID,
    @Column(name = "file_name")
    val fileName: String,
    @Column(name = "size")
    val size: Long,
    @Column(name = "publication_id")
    val publicationId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "id", insertable = false, updatable = false)
    val publication: PublicationEntity,

    @OneToOne(mappedBy = "uploadedFileId", fetch = FetchType.LAZY)
    val fileDetails: FileDetailsEntity

)