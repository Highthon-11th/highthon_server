package com.highthon.server.domain.user.controller

import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.dto.request.request.CreateEmailUserRequest
import com.highthon.server.global.jwt.model.dto.response.TokenResponse
import com.highthon.server.domain.user.model.dto.request.request.EmailLoginRequest
import com.highthon.server.domain.user.model.dto.request.request.OAuthLoginRequest
import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.model.mapper.UserMapper
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.domain.user.service.UserAuthService
import com.highthon.server.global.auth.CustomUserDetails
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "User")
@RestController
class UserController(
    private val userAuthService: UserAuthService,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    companion object {
        private const val PREFIX = "/user"
    }

    @GetMapping("$PREFIX/{id}")
    fun getUser(@PathVariable id: UUID,
    ): UserResponse {
       val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()

        return userMapper.toResponse(user)
    }
}