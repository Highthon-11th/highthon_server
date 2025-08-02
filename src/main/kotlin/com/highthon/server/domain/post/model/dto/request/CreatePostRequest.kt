package com.highthon.server.domain.post.model.dto.request

import com.highthon.server.domain.post.model.constant.PostType
import org.springframework.web.multipart.MultipartFile

data class CreatePostRequest(
    val title: String,
    val type: PostType,
    val content: String,
    val tagIdList: List<Long>,
    val image: MultipartFile?
)
