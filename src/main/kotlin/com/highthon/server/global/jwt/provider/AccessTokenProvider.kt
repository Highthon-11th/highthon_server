package com.highthon.server.global.jwt.provider

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import com.highthon.server.global.jwt.exception.InvalidTokenException
import com.highthon.server.global.jwt.exception.TokenExpirationException
import com.highthon.server.global.jwt.properties.JwtProperties
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class AccessTokenProvider(
    private val jwtProperties: JwtProperties,
    private val userDetailsService: UserDetailsService,
) {
    fun createToken(userId: UUID): String {
        val now = Instant.now()
        val expirationDate = now.plus(jwtProperties.accessTokenExpirationHours, ChronoUnit.HOURS)

        return Jwts.builder().let {
            it.subject(userId.toString())
            it.issuedAt(Date.from(now))
            it.signWith(getSecretKey())
            it.expiration(Date.from(expirationDate))
        }.compact()
    }

    @Throws(InvalidTokenException::class, TokenExpirationException::class)
    fun validateTokenOrThrow(token: String) : Boolean {
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

    fun getUserIdFromToken(token: String): UUID? {
        val claims = Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
        return UUID.fromString(claims.subject)
    }

    private fun getSecretKey(): SecretKey {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.accessTokenSecretKey))
    }


    fun getAuthentication(accessToken: String): Authentication {
        val username = getUserIdFromToken(accessToken).toString()
        val userDetails = userDetailsService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }
}
