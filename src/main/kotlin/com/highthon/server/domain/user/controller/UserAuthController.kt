package com.highthon.server.domain.user.controller

import com.highthon.server.domain.user.model.dto.request.request.CreateEmailUserRequest
import com.highthon.server.global.jwt.model.dto.response.TokenResponse
import com.highthon.server.domain.user.model.dto.request.request.EmailLoginRequest
import com.highthon.server.domain.user.model.dto.request.request.OAuthLoginRequest
import com.highthon.server.domain.user.service.UserAuthService
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User Auth")
@RestController
class UserAuthController(
    private val userAuthService: UserAuthService,
) {
    companion object {
        private const val PREFIX = "/auth"
    }

    @PostMapping("$PREFIX/register/email")
    fun registerEmailUser(
        @RequestBody @Valid request: CreateEmailUserRequest
    ): TokenResponse {
        return userAuthService.createEmailUser(request)
    }

    @PostMapping("$PREFIX/login/email")
    fun loginEmailUser(
        @RequestBody @Valid request: EmailLoginRequest
    ): TokenResponse {
        return userAuthService.loginEmailUser(request)
    }

    @PostMapping("$PREFIX/login/oauth")
    fun loginOAuthUser(
        @Param("provider") provider: OAuthProvider,
        @RequestBody @Valid request: OAuthLoginRequest
    ): TokenResponse {
        return userAuthService.loginOAuthUser(provider, request)
    }

    @PostMapping("$PREFIX/refresh")
    fun refreshAccessToken(
        @CookieValue("refreshToken") refreshToken: String
    ): TokenResponse {
        return userAuthService.refresh(refreshToken)
    }
}