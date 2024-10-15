package com.beemer.seoulbike

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SeoulbikeApplication

fun main(args: Array<String>) {
    runApplication<SeoulbikeApplication>(*args)
}
