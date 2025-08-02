package com.highthon.server.global.jwt.exception

import com.highthon.server.global.exception.BasicException
import org.springframework.http.HttpStatus


class InvalidTokenException : BasicException(HttpStatus.UNAUTHORIZED, "Invalid Token")
