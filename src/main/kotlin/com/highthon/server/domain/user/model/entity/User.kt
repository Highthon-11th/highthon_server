package com.highthon.server.domain.user.model.entity

import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.global.oauth.model.constant.OAuthProvider
import jakarta.persistence.*
import org.hibernate.usertype.UserType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity(name = "users")
@EntityListeners(AuditingEntityListener::class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
class User(
    @Id
    val id: UUID = UUID.randomUUID(),

    val name: String,

    @Column(unique = true)
    var email: String,

    val password: String?,

    @Enumerated(EnumType.STRING)
    val provider: OAuthProvider?,

    val providerId: String?,


    @Enumerated(EnumType.STRING)
    val role: UserRole,

    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @LastModifiedDate
    var updatedDate: Instant? = null,
) {}