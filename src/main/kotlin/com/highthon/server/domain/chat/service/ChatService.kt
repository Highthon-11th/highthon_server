package com.highthon.server.domain.chat.service

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Firestore
import com.highthon.server.domain.chat.model.dto.ChatRoom
import com.highthon.server.domain.user.model.entity.Mentee
import com.highthon.server.domain.user.model.entity.Mentor
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatService(
    private val firestore: Firestore,
) {

    fun createChatRoom(
        chatRoomId: UUID,
        mentorId: UUID,
        menteeId: UUID,
    ): UUID {
        val chatRoom= ChatRoom(
            mentorId = mentorId.toString(),
            menteeId = menteeId.toString(),
            createdDate = Timestamp.now()
        )

        firestore.collection("chatRooms")
            .document(chatRoomId.toString())
            .set(chatRoom)

        return chatRoomId
    }

//    fun sendMessage(
//    ): String {
//
//    }
}