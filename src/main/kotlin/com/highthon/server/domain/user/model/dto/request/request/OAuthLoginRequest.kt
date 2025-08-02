package com.highthon.server.domain.user.model.dto.request.request

import jakarta.validation.constraints.NotBlank

data class OAuthLoginRequest(
    @field:NotBlank
    val idToken: String
)
