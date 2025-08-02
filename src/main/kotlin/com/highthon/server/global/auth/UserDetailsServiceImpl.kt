package com.highthon.server.global.auth

import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user = userRepository.findByIdOrNull(UUID.fromString(username)) ?: throw UserNotFoundException()

        return CustomUserDetails(
            username = user.email,
            userId = user.id,
            role = user.role
        )
    }
}
