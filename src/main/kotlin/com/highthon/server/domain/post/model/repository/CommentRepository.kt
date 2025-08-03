package com.highthon.server.domain.post.model.repository

import com.highthon.server.domain.post.model.entity.Comment
import com.highthon.server.domain.post.model.entity.Post
import com.highthon.server.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

interface CommentRepository: JpaRepository<Comment, UUID> {
}