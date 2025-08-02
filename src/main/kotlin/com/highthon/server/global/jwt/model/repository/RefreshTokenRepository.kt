package com.highthon.server.global.jwt.model.repository

import com.highthon.server.global.jwt.model.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RefreshTokenRepository: CrudRepository<RefreshToken, String> {
    fun findByRefreshToken(refreshToken: String): RefreshToken?
    fun findByRefreshTokenAndUserId(refreshToken: String, userId: UUID): RefreshToken?
    fun existsByRefreshTokenAndUserId(refreshToken: String, userId: UUID): Boolean
}
