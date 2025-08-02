package com.highthon.server.domain.post.model.mapper

import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.entity.Post
import com.highthon.server.domain.post.model.entity.PostTag
import com.highthon.server.domain.post.model.repository.TagRepository
import com.highthon.server.global.s3.service.S3Service
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PostMapper(
    private val s3Service: S3Service,
    private val tagRepository: TagRepository,

    ) {

    fun toEntity(
        request: CreatePostRequest,
    ): Post {
        val imageKey =  if (request.image != null) {
            val imageKey = s3Service.uploadFile(
                "post/image",
                request.image
            )
            imageKey
        }
        else {
            null
        }


        val post = Post(
            title = request.title,
            type = request.type,
            content = request.content,
            author = null,
            imageKey = imageKey
        )

        val tagList = request.tagIdList.map { tagId ->
            val tag = tagRepository.findByIdOrNull(tagId)
                ?: throw IllegalArgumentException("Tag not found with id: $tagId")
            PostTag(
                post = post,
                tag = tag
            )
        }

        post.tagList.addAll(tagList)


        return post
    }

    @Transactional(readOnly = true)
    fun toResponse(
        post: Post,
    ): PostResponse {


        return PostResponse(
            id = post.id,
            title = post.title,
            type = post.type,
            content = post.content,

            authorId = post.author!!.id,
            authorName = post.author!!.name,
            authorType = post.author!!.role,

            imageUrl = post.imageKey?.let { s3Service.getUrl(it) },

            createdDate = post.createdDate,
            updatedDate = post.updatedDate,

            tags = post.tagList.map { it.tag!!.label }
        )
    }
}