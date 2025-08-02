package com.highthon.server.domain.user.model.dto.request.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateEmailUserRequest(
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val name: String,
)