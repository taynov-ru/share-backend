package ru.taynov.share.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
class MinioConfigProperties(
    val url: String,
    val username: String,
    val password: String,
    val bucket: String,
    val presignedUrlExpiry: Int,
)