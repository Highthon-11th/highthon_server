package com.highthon.server.global.jwt.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash("refresh-token")
class RefreshToken(
    @Id
    @Indexed
    val refreshToken: String,

    @Indexed
    val userId: UUID,

    @TimeToLive
    val expiration: Long
) {
}
