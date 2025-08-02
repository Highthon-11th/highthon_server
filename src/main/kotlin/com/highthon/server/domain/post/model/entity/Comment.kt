package com.highthon.server.domain.post.model.entity

import com.highthon.server.domain.user.model.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity(name = "comments")
@EntityListeners(AuditingEntityListener::class)
class Comment(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(columnDefinition = "TEXT")
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val author: User,

    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @LastModifiedDate
    var updatedDate: Instant? = null,
)