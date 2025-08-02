package com.highthon.server.global.auth

import com.highthon.server.domain.user.model.constant.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class CustomUserDetails(
    val userId: UUID,
    private val username: String,
    private val role: UserRole,
) : UserDetails {


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList(role.value)
    }

    override fun getUsername() = username
    override fun getPassword() = null


    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
