package com.highthon.server.domain.user.model.entity

import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import jakarta.persistence.*

@Entity
@DiscriminatorValue("MENTEE")
class Mentee(
    name: String,
    email: String,
    password: String?,
    provider: OAuthProvider,
    providerId: String?,
    introduce: String,
    description: String,
    profileImageUrl: String? = null,
) : User(
    name = name,
    email = email,
    password = password,
    role = UserRole.MENTEE,
    provider = provider,
    providerId = providerId,
    introduce = introduce,
    description = description,
    profileImageUrl = profileImageUrl,
) {
    @OneToMany(mappedBy = "mentee", cascade = [CascadeType.ALL], orphanRemoval = true)
    val mentorList: MutableList<Mentoring> = mutableListOf()
}