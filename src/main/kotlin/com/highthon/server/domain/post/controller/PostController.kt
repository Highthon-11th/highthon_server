package com.highthon.server.domain.post.controller

import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.entity.Post
import com.highthon.server.domain.post.model.mapper.PostMapper
import com.highthon.server.domain.post.service.PostService
import com.highthon.server.domain.post.service.TagService
import com.highthon.server.global.auth.CustomUserDetails
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "Post")
class PostController(
    private val postService: PostService,
    private val tagService: TagService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val PREFIX = "/post"
    }

    @PostMapping("${PREFIX}/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun create(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @ModelAttribute request: CreatePostRequest,
    ): PostResponse {
        val post = postService.createPost(
            userDetails,
            request
        )

        return post
    }

    @GetMapping(PREFIX)
    fun getPostsByTag(
        @RequestParam(value = "tag", required = false) tagList: List<Long>?,
    ): List<PostResponse> {

        val postList = if (tagList.isNullOrEmpty()) {
            postService.getPostList()
        } else {
            postService.getPostsByTags(tagList)
        }

        return postList
    }

    @GetMapping("${PREFIX}/my")
    fun getMyPosts(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): List<PostResponse> {

        val postList = postService.getMyPosts(userDetails)

        return postList
    }
}