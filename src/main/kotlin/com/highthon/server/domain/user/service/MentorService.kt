package com.highthon.server.domain.user.service

import com.highthon.server.domain.user.model.constant.UserRole
import com.highthon.server.domain.user.model.dto.response.UserResponse
import com.highthon.server.domain.user.model.entity.Mentor
import com.highthon.server.domain.user.model.mapper.UserMapper
import com.highthon.server.domain.user.model.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MentorService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {

    fun getMentorList(): List<UserResponse> {
        val mentors= userRepository.findByRole(UserRole.MENTOR).map { it as Mentor }

        return mentors.map {
            userMapper.toResponse(it)
        }
    }

}