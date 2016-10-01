package com.oleske

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class WnkApplication

fun main(args: Array<String>) {
    SpringApplication.run(WnkApplication::class.java, *args)
}
