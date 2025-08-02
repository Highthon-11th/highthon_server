package com.highthon.server.global.exception

import java.time.Instant


data class ExceptionResponse(
    val status: Int,
    val message: String?,
    val timestamp: Instant = Instant.now(),
    val path: String,
)