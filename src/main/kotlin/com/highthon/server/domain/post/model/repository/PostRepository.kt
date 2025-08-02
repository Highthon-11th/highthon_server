package com.highthon.server.domain.post.model.repository

import com.highthon.server.domain.post.model.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

interface PostRepository: JpaRepository<Post, UUID> {

    // For tag IDs
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tagList pt WHERE pt.tag.id IN :tagIds")
    fun findByTagIds(@Param("tagIds") tagIds: List<Long>): List<Post>

}