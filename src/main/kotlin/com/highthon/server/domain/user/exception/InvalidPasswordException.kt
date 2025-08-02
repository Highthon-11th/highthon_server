package com.highthon.server.domain.user.exception

import com.highthon.server.global.exception.BasicException
import org.springframework.http.HttpStatus

class InvalidPasswordException : BasicException(HttpStatus.BAD_REQUEST, "Invalid password") {}