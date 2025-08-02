package com.highthon.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HighthonServerApplication

fun main(args: Array<String>) {
	runApplication<HighthonServerApplication>(*args)
}
