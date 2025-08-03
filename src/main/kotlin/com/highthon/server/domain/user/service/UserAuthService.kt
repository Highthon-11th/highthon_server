package com.highthon.server.domain.user.service

import com.highthon.server.domain.user.exception.InvalidPasswordException
import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.domain.user.model.dto.request.request.CreateEmailUserRequest
import com.highthon.server.global.jwt.model.dto.response.TokenResponse
import com.highthon.server.domain.user.model.dto.request.request.EmailLoginRequest
import com.highthon.server.domain.user.model.dto.request.request.OAuthLoginRequest
import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.model.entity.Mentee
import com.highthon.server.domain.user.model.entity.User
import com.highthon.server.domain.user.model.mapper.UserMapper
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.global.auth.CustomUserDetails
import com.highthon.server.global.jwt.exception.InvalidTokenException
import com.highthon.server.global.jwt.provider.AccessTokenProvider
import com.highthon.server.global.jwt.provider.RefreshTokenProvider
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import com.highthon.server.global.oauth.provider.IdTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAuthService(
    private val userRepository: UserRepository,
    private val idTokenProvider: IdTokenProvider,
    private val accessTokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper,

    ) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun loginEmailUser(emailLoginRequest: EmailLoginRequest): TokenResponse {
        val user = userRepository.findByEmail(emailLoginRequest.email) ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(emailLoginRequest.password, user.password)) {
            throw InvalidPasswordException()
        }

        val accessToken = accessTokenProvider.createToken(user.id)
        val refreshToken = refreshTokenProvider.createToken(user.id)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }


    fun loginOAuthUser(provider: OAuthProvider, oAuthLoginRequest: OAuthLoginRequest): TokenResponse {


        val payload = idTokenProvider.verifyToken(provider, oAuthLoginRequest.idToken) ?: throw InvalidTokenException()

        val providerId = payload["sub"].toString()

        val user = userRepository.findByProviderAndProviderId(provider, providerId) ?: Mentee(
            email = payload["email"]?.toString() ?: "",
            name = payload["nickname"]?.toString() ?: "",
            provider = provider,
            providerId = providerId,
            password = null,
            introduce = "",
            description = "",
            profileImageUrl = payload["picture"]?.toString(),


            ).let {
            userRepository.save(it)
        }


        val accessToken = accessTokenProvider.createToken(user.id)
        val refreshToken = refreshTokenProvider.createToken(user.id)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }


    fun refresh(refreshToken: String): TokenResponse {

        refreshTokenProvider.validateTokenOrThrow(refreshToken)

        val user = refreshTokenProvider.getUserIdFromToken(refreshToken).let {
            userRepository.findByIdOrNull(it) ?: throw UserNotFoundException()
        }

        if (!refreshTokenProvider.matchToken(refreshToken, user.id)) {
            throw InvalidTokenException()
        }

        refreshTokenProvider.deleteToken(refreshToken)

        val newAccessToken = accessTokenProvider.createToken(user.id)
        val newRefreshToken = refreshTokenProvider.createToken(user.id)

        return TokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }

    fun createEmailUser(request: CreateEmailUserRequest): TokenResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserNotFoundException()
        }

        val encodedPassword = passwordEncoder.encode(request.password)

        val user = User(
            email = request.email,
            password = encodedPassword,
            name = request.name,
            provider = OAuthProvider.EMAIL,
            providerId = request.email, // Using email as providerId for email users
            role = UserRole.MENTEE,
            introduce = "",
            description = "",

            ).let {
            userRepository.save(it)
        }

        val accessToken = accessTokenProvider.createToken(user.id)
        val refreshToken = refreshTokenProvider.createToken(user.id)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun getMe(userDetails: CustomUserDetails): UserResponse {
        val user = userRepository.findByIdOrNull(userDetails.userId)!!;

        return userMapper.toResponse(user)
    }
}
