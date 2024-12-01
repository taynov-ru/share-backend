package ru.taynov.share.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.taynov.share.config.properties.MinioConfigProperties

@Configuration
@EnableConfigurationProperties(MinioConfigProperties::class)
class MinioConfig {

    @Bean
    fun minioClient(minioProperties: MinioConfigProperties): MinioClient {
        val minioClient = MinioClient.builder()
            .endpoint(minioProperties.url)
            .credentials(minioProperties.username, minioProperties.password)
            .build()

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.bucket).build())
        }
        return minioClient
    }
}