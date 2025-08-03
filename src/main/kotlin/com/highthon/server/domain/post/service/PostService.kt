package com.highthon.server.domain.post.service

import com.highthon.server.domain.post.model.dto.request.AddCommentRequest
import com.highthon.server.domain.post.model.dto.request.CreatePostRequest
import com.highthon.server.domain.post.model.dto.response.CommentResponse
import com.highthon.server.domain.post.model.dto.response.PostResponse
import com.highthon.server.domain.post.model.entity.Comment
import com.highthon.server.domain.post.model.mapper.CommentMapper
import com.highthon.server.domain.post.model.mapper.PostMapper
import com.highthon.server.domain.post.model.repository.CommentRepository
import com.highthon.server.domain.post.model.repository.PostRepository
import com.highthon.server.domain.user.exception.UserNotFoundException
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.global.auth.CustomUserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PostService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
    private val commentMapper: CommentMapper,
    private val commentRepository: CommentRepository,
) {
    fun createPost(userDetails: CustomUserDetails, request: CreatePostRequest): PostResponse {
        val user = userRepository.findByIdOrNull(userDetails.userId) ?: throw UserNotFoundException()

        val post = postMapper.toEntity(request).let {
            it.author = user

            postRepository.save(it)
        }
        return postMapper.toResponse(post)
    }

    @Transactional(readOnly = true)
    fun getPostList(): List<PostResponse> {
        return postRepository.findAll().map { postMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getPostsByTags(tagList: List<Long>): List<PostResponse> {
        return postRepository.findByTagIds(tagList).map { postMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getMyPosts(userDetails: CustomUserDetails): List<PostResponse> {
        val user = userRepository.findByIdOrNull(userDetails.userId) ?: throw UserNotFoundException()

        return postRepository.findByAuthor(user).map { postMapper.toResponse(it) }
    }

    @Transactional
    fun createComment(userDetails: CustomUserDetails, request: AddCommentRequest): CommentResponse {
        val user = userRepository.findByIdOrNull(userDetails.userId) ?: throw UserNotFoundException()
        val post = postRepository.findByIdOrNull(request.postId) ?: throw IllegalArgumentException("Post not found")

        val comment = Comment(
            content = request.content,
            post = post,
            author = user
        ).let {
            commentRepository.save(it)
        }

        return commentMapper.toResponse(comment)
    }

    @Transactional(readOnly = true)
    fun getCommentsByPostId(postId: UUID): List<CommentResponse> {
        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException("Post not found")
        return post.commentList.map { commentMapper.toResponse(it) }
    }

}
