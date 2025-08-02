package com.highthon.server.domain.post.model.entity

import com.highthon.server.domain.post.model.constant.PostType
import com.highthon.server.domain.user.model.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Post(
    @Id
    val id: UUID = UUID.randomUUID(),

    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    var imageKey: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var author: User?,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    val tagList: MutableList<PostTag> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    val commentList: MutableList<Comment> = mutableListOf(),


    @Enumerated(EnumType.STRING)
    val type: PostType,

    @CreatedDate
    var createdDate: Instant = Instant.now(),
    @LastModifiedDate
    var updatedDate: Instant? = null,
)
