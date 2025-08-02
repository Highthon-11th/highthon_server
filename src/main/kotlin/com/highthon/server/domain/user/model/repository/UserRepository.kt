package com.highthon.server.domain.user.model.repository

import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.domain.user.model.entity.User
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByProviderAndProviderId(provider: OAuthProvider, providerId: String): User?
    fun existsByProviderAndProviderId(provider: OAuthProvider, providerId: String): Boolean

    fun findByRole(role: UserRole): List<User>
}
