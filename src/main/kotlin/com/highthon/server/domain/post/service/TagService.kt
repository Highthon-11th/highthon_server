package com.highthon.server.domain.post.service

import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.dto.response.TagResponse
import com.highthon.server.domain.post.model.entity.Tag
import com.highthon.server.domain.post.model.mapper.PostMapper
import com.highthon.server.domain.post.model.repository.PostRepository
import com.highthon.server.domain.post.model.repository.TagRepository
import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.global.auth.CustomUserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TagService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
    private val tagRepository: TagRepository,
) {

    fun createTag(
        label: String,
    ): TagResponse {
        val tag = Tag(
            label = label,
        ).let {
            tagRepository.save(it)
        }

        return TagResponse(
            id = tag.id,
            label = tag.label,
        )
    }

    fun getTagList(): List<TagResponse> {
        return tagRepository.findAll().map {
            TagResponse(
                id = it.id,
                label = it.label,
            )
        }
    }

}