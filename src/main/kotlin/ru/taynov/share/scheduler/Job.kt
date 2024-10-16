package ru.taynov.share.scheduler

import ru.taynov.share.dto.JobProperties

interface Job {
    fun execute()
    fun getProperties(): JobProperties
}