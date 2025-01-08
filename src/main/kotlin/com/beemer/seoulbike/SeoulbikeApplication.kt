package com.beemer.seoulbike

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
class SeoulbikeApplication

fun main(args: Array<String>) {
    runApplication<SeoulbikeApplication>(*args)
}
