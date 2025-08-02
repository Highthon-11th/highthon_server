package com.highthon.server.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${server.port}")
    private val port: String,
) {
    companion object {
        private const val SECURITY_SCHEME_NAME = "authorization"
    }

    @Bean
    fun openAPI(): OpenAPI {
        val local = Server().url("http://localhost:${port}")
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        SECURITY_SCHEME_NAME, SecurityScheme()
                            .name(SECURITY_SCHEME_NAME)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )

            .addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .servers(
                listOf(
                    local
                )
            )
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("FitBoarding API")
            .description("fit-boarding api document")
            .version("0.0.1")
    }
}
