package ru.taynov.share.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "files")
data class FileEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_uuid", unique = true)
    val fileUuid: UUID? = null,
    @Column(name = "file_name")
    val fileName: String,
    @Column(name = "size")
    val size: Long,
    @Column(name = "deleted")
    val deleted: Boolean = false,

    @OneToOne(mappedBy = "uploadedFileId", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    val fileDetails: FileDetailsEntity? = null

)