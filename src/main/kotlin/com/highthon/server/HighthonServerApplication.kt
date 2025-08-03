package com.highthon.server

import com.highthon.server.global.firebase.config.FirestoreConfig
import com.highthon.server.global.firebase.service.FirestoreService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
class HighthonServerApplication

fun main(args: Array<String>) {
    runApplication<HighthonServerApplication>(*args)
}

@RestController
class HealthCheckController(
    private val firestoreService: FirestoreService
) {

    @PostMapping("/save")
    fun saveRandomDocument(): ResponseEntity<String> {
        val data = mapOf(
            "name" to "ㅁ니아ㅓ라ㅣ}",
            "email" to "request.email"
        )
        val id = firestoreService.saveWithRandomId("users", data)
        return ResponseEntity.ok("Saved with ID: $id")
    }
}