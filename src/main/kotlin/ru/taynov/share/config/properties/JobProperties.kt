package ru.taynov.share.config.properties

abstract class JobProperties(
    open val name: String,
    open val timeout: Long,
    open val enabled: Boolean,
)