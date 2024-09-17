package ru.taynov.share

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShareApplication

fun main(args: Array<String>) {
	runApplication<ShareApplication>(*args)
}
