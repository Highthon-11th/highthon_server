package com.highthon.server.domain.user.model.dto.request.request

import jakarta.validation.constraints.NotBlank

data class CreateOAuthUserRequest(
    @field:NotBlank
    val idToken: String,
)