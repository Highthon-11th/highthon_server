package com.highthon.server.domain.post.model.entity

import java.io.Serializable
import java.util.*
import jakarta.persistence.Embeddable

@Embeddable
data class PostTagId(
    val postId: UUID? = null,  // 기본값을 null로 변경
    val tagId: Long? = null    // 기본값을 null로 변경
) : Serializable