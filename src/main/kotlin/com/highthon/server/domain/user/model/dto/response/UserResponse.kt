package com.highthon.server.domain.user.model.dto.response

import java.time.Instant

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val role: String,
    val createdDate: Instant,
    val updatedDate: Instant?

)