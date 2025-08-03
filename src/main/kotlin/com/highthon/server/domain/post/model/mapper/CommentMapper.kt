package com.highthon.server.domain.post.model.mapper

import com.highthon.server.domain.post.model.dto.response.CommentResponse
import com.highthon.server.domain.post.model.entity.Comment
import org.springframework.stereotype.Component

@Component
class CommentMapper {
    
    fun toResponse(comment: Comment): CommentResponse {
        return CommentResponse(
            id = comment.id,
            content = comment.content,
            postId = comment.post.id,
            
            createdDate = comment.createdDate,
            updatedDate = comment.updatedDate,
            
            authorId = comment.author.id,
            authorName = comment.author.name,
            authorType = comment.author.role
        )
    }
}