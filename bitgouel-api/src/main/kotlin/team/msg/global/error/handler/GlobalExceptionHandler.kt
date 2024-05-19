package team.msg.global.error.handler

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import team.msg.global.error.DataErrorResponse
import team.msg.global.error.ErrorResponse
import team.msg.global.error.NoHandlerErrorResponse
import team.msg.global.error.ValidationErrorResponse
import team.msg.global.error.exception.BitgouelException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BitgouelException::class)
    fun bitgouelExceptionHandler(e: BitgouelException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.valueOf(e.status))

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ValidationErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<DataErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<NoHandlerErrorResponse> =
        ResponseEntity(ErrorResponse.of(e), HttpStatus.FORBIDDEN)

}