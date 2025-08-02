package com.highthon.server.domain.post.model.repository

import com.highthon.server.domain.post.model.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

interface TagRepository: JpaRepository<Tag, Long> {}