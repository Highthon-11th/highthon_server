package com.highthon.server.domain.user.model.mapper

import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.domain.user.model.dto.request.request.CreateEmailUserRequest
import com.highthon.server.domain.user.model.dto.request.request.CreateOAuthUserRequest
import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.model.entity.User
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import com.highthon.server.global.oauth.provider.IdTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder,
    private val idTokenProvider: IdTokenProvider,
) {
    fun toEntity(
        request: CreateEmailUserRequest,
        role: UserRole,
    ): User {

        val user = with(request) {
            User(
                name = name,
                email = email,
                password = passwordEncoder.encode(password),
                provider = null,
                providerId = null,
                role = role,
                introduce = "",
                description = "",
            )
        }

        return user
    }

    fun toEntity(
        request: CreateOAuthUserRequest,
        provider: OAuthProvider,
        role: UserRole,
    ): User {
        val payload = idTokenProvider.verifyToken(
            provider,
            request.idToken
        )

        val sub = payload["sub"].toString()
        val email = payload["email"].toString()

        val name = when (provider) {
            OAuthProvider.KAKAO -> payload["nickname"].toString()
            else -> throw kotlin.IllegalArgumentException("Not supported provider")
        }

        val user = User(
            name = name,
            email = email,
            provider = provider,
            providerId = sub,
            password = null,
            role = role,
            introduce = "",
            description = "",
        )

        return user
    }


    fun toResponse(
        user: User,
    ): UserResponse {
        return UserResponse(
            id = user.id.toString(),
            email = user.email,
            name = user.name,
            role = user.role.name,
            createdDate = user.createdDate,
            updatedDate = user.updatedDate,

            introduce = user.introduce,
            description = user.description,
            profileImageUrl = user.profileImageUrl
        )
    }
}