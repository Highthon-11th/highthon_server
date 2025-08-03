package com.highthon.server.domain.chat.model.dto

import java.security.Timestamp

data class ChatMessage(
    val senderId: String,
    val content: String,
    val createdDate: Timestamp,
)