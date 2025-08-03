package com.highthon.server.domain.post.model.dto.response

import com.highthon.server.domain.user.model.constant.UserRole
import java.time.Instant
import java.util.UUID

data class CommentResponse(
    val id: UUID,
    val content: String,
    val postId: UUID,
    
    val createdDate: Instant,
    val updatedDate: Instant?,
    
    val authorId: UUID,
    val authorName: String,
    val authorType: UserRole,
)