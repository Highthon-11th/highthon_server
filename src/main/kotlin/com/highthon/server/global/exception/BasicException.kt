package com.highthon.server.global.exception

import org.springframework.http.HttpStatus


open class BasicException(
    val status: HttpStatus,
    override val message: String
): RuntimeException()
