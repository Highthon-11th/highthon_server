package com.highthon.server.global.jwt.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("jwt")
data class JwtProperties(
    val accessTokenSecretKey: String,
    val accessTokenExpirationHours: Long,

    val refreshTokenSecretKey: String,
    val refreshTokenExpirationHours: Long,
)
