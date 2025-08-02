package com.highthon.server.domain.post.controller

import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.dto.response.TagResponse
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
@Tag(name = "Tag")
class TagController(
    private val tagService: TagService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val PREFIX = "/tag"
    }

    @PostMapping("${PREFIX}/create")
    fun create(
        @RequestParam label: String
    ): TagResponse {
        val tag = tagService.createTag(label)
        return tag
    }

    @GetMapping(PREFIX)
    fun getTagList(): List<TagResponse> {
        val tagList = tagService.getTagList()
        logger.info("tagList: $tagList")

        return tagList
    }
}