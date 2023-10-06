package bitgouel.team.msg.global.error.handler

import bitgouel.team.msg.global.error.ErrorResponse
import bitgouel.team.msg.global.error.exception.BitgouelException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BitgouelException::class)
    fun bitgouelExceptionHandler(e: BitgouelException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(e.errorCode.message, e.errorCode.status),
            HttpStatus.valueOf(e.errorCode.status)
        )
}