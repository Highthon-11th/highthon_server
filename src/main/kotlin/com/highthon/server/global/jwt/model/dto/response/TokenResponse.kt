package com.highthon.server.global.jwt.model.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
