package com.beemer.seoulbike

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SeoulbikeApplication

fun main(args: Array<String>) {
    runApplication<SeoulbikeApplication>(*args)
}
