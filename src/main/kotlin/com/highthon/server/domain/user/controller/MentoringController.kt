package com.highthon.server.domain.user.controller

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Firestore
import com.highthon.server.domain.chat.service.ChatService
import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.model.entity.Mentee
import com.highthon.server.domain.user.model.entity.Mentor
import com.highthon.server.domain.user.model.entity.Mentoring
import com.highthon.server.domain.user.model.mapper.UserMapper
import com.highthon.server.domain.user.model.repository.UserRepository
import com.highthon.server.domain.user.service.MentorService
import com.highthon.server.global.auth.CustomUserDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class SendMessageRequest(
    val chatRoomId: String,
    val message: String,
)

@RestController
class MentoringController(
    private val chatService: ChatService,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,

    private val firestore: Firestore
) {

    companion object {
        private const val PREFIX = "/mentoring"
    }

    @PostMapping("${PREFIX}/create")
    @Transactional
    fun createMentoring(
        @RequestParam mentorId: UUID,
        @RequestParam menteeId: UUID,
    ) {

        val mentor = userRepository.findByIdOrNull(mentorId)!! as Mentor
        val mentee = userRepository.findByIdOrNull(menteeId)!! as Mentee

        val chatRoomId = UUID.randomUUID()

        val mentoring = Mentoring(
            mentor = mentor,
            mentee = mentee,
            chatRoomId = chatRoomId
        )

        mentee.mentorList.add(mentoring)
        mentor.menteeList.add(mentoring)


        chatService.createChatRoom(chatRoomId, mentorId, menteeId)
    }


    @PostMapping("${PREFIX}/chat/send")
    @Transactional
    fun sendMessage(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: SendMessageRequest
    ) {
        val user = userRepository.findByIdOrNull(userDetails.userId)!!

        val message = mapOf(
            "senderId" to user.id.toString(),
            "message" to request.message,
            "createdDate" to Timestamp.now()
        )

        firestore.collection("chatRooms")
            .document(request.chatRoomId)
            .collection("messages")
            .add(message)


    }

    @GetMapping("${PREFIX}/list")
    fun getMentoringList(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): List<UserResponse> {

        val user = userRepository.findByIdOrNull(userDetails.userId)!!

        return when (user) {
            is Mentor -> user.menteeList.map {
                userMapper.toResponse(it.mentee)
            }
            is Mentee -> user.mentorList.map {
                userMapper.toResponse(it.mentor)
            }
            else -> throw IllegalArgumentException("Invalid user type")
        }

    }

    @GetMapping("${PREFIX}/chat/list")
    fun getMentoringChatIdList(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): List<UUID> {

        val user = userRepository.findByIdOrNull(userDetails.userId)!!

        return when (user) {
            is Mentor -> user.menteeList.map {
                it.chatRoomId
            }
            is Mentee -> user.mentorList.map {
                it.chatRoomId
            }
            else -> throw IllegalArgumentException("Invalid user type")
        }

    }

}