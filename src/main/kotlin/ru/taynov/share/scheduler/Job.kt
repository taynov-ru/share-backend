package ru.taynov.share.scheduler

import ru.taynov.share.config.properties.JobProperties

interface Job {
    fun execute()
    fun getProperties(): JobProperties
}