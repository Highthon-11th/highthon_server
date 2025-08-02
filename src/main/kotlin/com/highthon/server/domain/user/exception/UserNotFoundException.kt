package com.highthon.server.domain.user.exception

import com.highthon.server.global.exception.BasicException
import org.springframework.http.HttpStatus

class UserNotFoundException : BasicException(HttpStatus.NOT_FOUND, "User not found")