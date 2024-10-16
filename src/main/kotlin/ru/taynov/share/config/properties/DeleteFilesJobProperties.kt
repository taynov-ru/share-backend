package ru.taynov.share.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "jobs.delete-files")
data class DeleteFilesJobProperties @ConstructorBinding constructor(
    override val name: String,
    override val timeout: Long,
    override val enabled: Boolean
) : JobProperties(name, timeout, enabled)