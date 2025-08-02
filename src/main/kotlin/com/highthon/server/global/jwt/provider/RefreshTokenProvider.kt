package com.highthon.server.global.jwt.provider

import com.highthon.server.global.jwt.exception.InvalidTokenException
import com.highthon.server.global.jwt.exception.TokenExpirationException
import com.highthon.server.global.jwt.model.entity.RefreshToken
import com.highthon.server.global.jwt.model.repository.RefreshTokenRepository
import com.highthon.server.global.jwt.properties.JwtProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class RefreshTokenProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun createToken(userId: UUID): String {
        val now = Instant.now()
        val expirationDate = now.plus(jwtProperties.refreshTokenExpirationHours, ChronoUnit.HOURS)

        val refreshToken = Jwts.builder().let {
            it.subject(userId.toString())
            it.issuedAt(Date.from(now))
            it.signWith(getSecretKey())
            it.expiration(Date.from(expirationDate))
        }.compact()

        RefreshToken(
            userId = userId,
            refreshToken = refreshToken,
            expiration = expirationDate.toEpochMilli()
        ).let {
            refreshTokenRepository.save(it)
        }

        return refreshToken
    }

    @Throws(InvalidTokenException::class, TokenExpirationException::class)
    fun validateTokenOrThrow(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: ExpiredJwtException) {
            throw TokenExpirationException()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }


    fun matchToken(token: String, userId: UUID): Boolean {
        return refreshTokenRepository.existsByRefreshTokenAndUserId(token, userId)
    }

    fun deleteToken(token: String) {
        refreshTokenRepository.deleteById(token)
    }

    fun getUserIdFromToken(token: String): UUID {
        val claims = Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
        return UUID.fromString(claims.subject)
    }

    private fun getSecretKey(): SecretKey {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.refreshTokenSecretKey))
    }
}
