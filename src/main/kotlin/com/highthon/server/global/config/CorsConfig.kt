package com.highthon.server.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import kotlin.apply

@Configuration
class CorsConfig {
    companion object {
        val CORS_ALLOW_URL = listOf(
            "http://localhost:5173",
            "http://localhost:4173",
            "https://fit-boarding.doyun.dev",
        )
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowedOrigins = CORS_ALLOW_URL
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
