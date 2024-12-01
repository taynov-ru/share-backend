package ru.taynov.share.service.impl

import io.minio.GetObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.StatObjectArgs
import io.minio.http.Method
import java.io.InputStream
import java.time.ZonedDateTime
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.taynov.share.config.properties.MinioConfigProperties
import ru.taynov.share.enums.FileExceptionCode.STORAGE_ERROR
import ru.taynov.share.service.StorageService

@Service
class StorageServiceImpl(
    private val minioClient: MinioClient,
    private val minioProperties: MinioConfigProperties
) : StorageService {

    override fun saveFile(file: MultipartFile, id: UUID?) {
        runCatching {
            val response = minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioProperties.bucket)
                    .`object`(id.toString())
                    .stream(file.inputStream, file.size, 0L)
                    .contentType(file.contentType)
                    .build()
            )

            if (response?.`object`() != id.toString()) {
                throw RuntimeException("Cannot get object id from S3: ${id.toString()}")
            }
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw STORAGE_ERROR.getException()
        }
    }

    override fun getFileUrl(id: UUID, filename: String): String? {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(minioProperties.bucket)
                .`object`(id.toString())
                .expiry(minioProperties.presignedUrlExpiry)
                .extraQueryParams(mapOf("response-content-disposition" to "attachment; filename=\"$filename\""))
                .build()
        )
    }

    override fun getFile(id: UUID): InputStream {
        runCatching {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(minioProperties.bucket)
                    .`object`(id.toString())
                    .build()
            )
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw STORAGE_ERROR.getException()
        }
    }

    override fun deleteFile(id: UUID) {
        runCatching {
            return minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(minioProperties.bucket)
                    .`object`(id.toString())
                    .build()
            )
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw STORAGE_ERROR.getException()
        }
    }

    override fun getFileUploadDate(id: UUID): ZonedDateTime {
        runCatching {
            return minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(minioProperties.bucket)
                    .`object`(id.toString())
                    .build()
            ).lastModified()
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw STORAGE_ERROR.getException()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StorageServiceImpl::class.java)
    }
}