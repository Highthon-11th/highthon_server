package com.highthon.server.global.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ExceptionResponse(
                status = HttpStatus.NOT_FOUND.value(),
                message = e.message,
                path = request.requestURI
            )
        )
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupportedException(e: HttpRequestMethodNotSupportedException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
            ExceptionResponse(
                status = HttpStatus.METHOD_NOT_ALLOWED.value(),
                message = e.message,
                path = request.requestURI
            )
        )
    }



    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Invalid Request Body",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = e.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage!!}" },
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(BasicException::class)
    fun handleBasicException(e: BasicException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(e.status).body(
            ExceptionResponse(
                status = e.status.value(),
                message = e.message,
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        e.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ExceptionResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = e.message,
                path = request.requestURI
            )
        )
    }
}