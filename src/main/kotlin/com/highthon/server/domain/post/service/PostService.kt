package com.highthon.server.domain.post.service

import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.entity.PostTag
import com.highthon.server.domain.post.model.entity.PostTagId
import com.highthon.server.domain.post.model.mapper.PostMapper
import com.highthon.server.domain.post.model.repository.PostRepository
import com.highthon.server.domain.post.model.repository.TagRepository
import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.global.auth.CustomUserDetails
import com.highthon.server.global.s3.service.S3Service
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.RequestEntity.post
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PostService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
) {
    fun createPost(userDetails: CustomUserDetails, request: CreatePostRequest): PostResponse {
        val user = userRepository.findByIdOrNull(userDetails.userId) ?: throw UserNotFoundException()

        val post = postMapper.toEntity(request).let {
            it.author = user

            postRepository.save(it)
        }
        return postMapper.toResponse(post)
    }

    fun getPostList(): List<PostResponse> {
        return postRepository.findAll().map { postMapper.toResponse(it) }
    }

    fun getPostsByTags(tagList: List<Long>): List<PostResponse> {
        return postRepository.findByTagIds(tagList).map { postMapper.toResponse(it) }
    }

}