package com.highthon.server.domain.user.model.entity

import java.io.Serializable
import java.util.*
import jakarta.persistence.Embeddable

@Embeddable
data class MentoringId(
    val mentorId: UUID = UUID.randomUUID(),
    val menteeId: UUID = UUID.randomUUID()
) : Serializable