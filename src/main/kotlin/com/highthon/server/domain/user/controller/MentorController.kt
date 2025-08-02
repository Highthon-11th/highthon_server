package com.highthon.server.domain.user.controller

import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.service.MentorService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Mentor")
@RestController
class MentorController(
    private val mentorService: MentorService,
) {

    companion object {
        private const val PREFIX = "/mentor"
    }

    @GetMapping("${PREFIX}/list")
    fun getMentorList(): List<UserResponse> {
        val mentorList = mentorService.getMentorList()

        return mentorList
    }
}