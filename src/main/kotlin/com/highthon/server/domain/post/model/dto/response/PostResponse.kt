package com.highthon.server.domain.post.model.dto.response

import com.highthon.server.domain.post.model.constant.PostType
import com.highthon.server.domain.user.model.constant.UserRole
import java.time.Instant
import java.util.UUID

data class PostResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val type: PostType,

    val createdDate: Instant,
    val updatedDate: Instant?,

    val imageUrl: String?,

    val tags: List<String>,

    val authorId: UUID,
    val authorName: String,
    val authorType: UserRole,
)