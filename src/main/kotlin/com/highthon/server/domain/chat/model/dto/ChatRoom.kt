package com.highthon.server.domain.chat.model.dto

import com.google.cloud.Timestamp

data class ChatRoom(
    val mentorId: String,
    val menteeId: String,
    val createdDate: Timestamp
)