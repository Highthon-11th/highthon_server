package com.highthon.server.domain.post.model.dto.request

import java.util.*

data class AddCommentRequest(
    val postId: UUID,
    val content: String,
)
