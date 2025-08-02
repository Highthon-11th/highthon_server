package com.highthon.server.global.oauth.provider

import com.fasterxml.jackson.databind.ObjectMapper
import com.highthon.server.global.jwt.exception.InvalidTokenException
import com.highthon.server.global.jwt.exception.TokenExpirationException
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import com.highthon.server.global.oauth.model.dto.response.OIDCPublicKeysResponse
import com.highthon.server.global.oauth.properties.KakaoProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*
import kotlin.collections.get


@Component
class IdTokenProvider(
    private val kakaoProperties: KakaoProperties,
) {
    private val kakaoOIDCClient = RestClient.create("https://kauth.kakao.com/.well-known/jwks.json")

    @Throws(InvalidTokenException::class, TokenExpirationException::class)
    fun verifyToken(provider: OAuthProvider, token: String): Claims {
        try {
            if (!validateTokenPayload(provider, token)) {
                throw InvalidTokenException()
            }

            val publicKey = getPublicKey(provider, token)

            val claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)

            return claims.payload
        } catch (e: ExpiredJwtException) {
            throw TokenExpirationException()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    private fun validateTokenPayload(provider: OAuthProvider, token: String): Boolean {
        val decoder = Base64.getDecoder()
        val splitToken = token.split(".")


        val payload = decoder.decode(splitToken[1]).toString(Charsets.UTF_8).let {
            ObjectMapper().readValue(it, HashMap::class.java)
        }

        return when (provider) {
            OAuthProvider.KAKAO -> payload["aud"] in kakaoProperties.appKeys && payload["iss"] == "https://kauth.kakao.com"
            OAuthProvider.GOOGLE -> throw NotImplementedError()
            OAuthProvider.APPLE -> throw NotImplementedError()
            OAuthProvider.EMAIL -> throw NotImplementedError()
        }
    }

    private fun getPublicKey(provider: OAuthProvider, token: String): PublicKey {
        val kid = getKidFromToken(token)

        val response = when (provider) {
            OAuthProvider.KAKAO -> kakaoOIDCClient.get().retrieve().body(OIDCPublicKeysResponse::class.java)!!
            OAuthProvider.GOOGLE -> throw NotImplementedError()
            OAuthProvider.APPLE -> throw NotImplementedError()
            OAuthProvider.EMAIL -> throw NotImplementedError()
        }

        val key = response.keys.find { it.kid == kid } ?: throw InvalidTokenException()

        val keyFactory = KeyFactory.getInstance("RSA")
        val decodeN = Base64.getUrlDecoder().decode(key.n)
        val decodeE = Base64.getUrlDecoder().decode(key.e)
        val n = BigInteger(1, decodeN)
        val e = BigInteger(1, decodeE)

        val keySpec = RSAPublicKeySpec(n, e)
        return keyFactory.generatePublic(keySpec)
    }

    private fun getKidFromToken(token: String): String {
        val decoder = Base64.getDecoder()

        val splitToken = token.split(".")


        val header = decoder.decode(splitToken[0]).toString(Charsets.UTF_8).let {
            ObjectMapper().readValue(it, HashMap::class.java)
        }


        return header["kid"].toString()
    }
}