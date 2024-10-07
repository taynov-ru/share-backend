package ru.taynov.share.service.impl

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import java.io.InputStream
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
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioProperties.bucket)
                    .`object`(id.toString())
                    .stream(file.inputStream, file.size, 0L)
                    .contentType(file.contentType)
                    .build()
            )
        }.getOrElse { ex ->
            log.error(ex.toString(), ex)
            throw STORAGE_ERROR.getException()
        }
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

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StorageServiceImpl::class.java)
    }
}