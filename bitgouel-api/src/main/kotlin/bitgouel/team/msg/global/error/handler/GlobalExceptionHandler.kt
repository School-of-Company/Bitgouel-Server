package bitgouel.team.msg.global.error.handler

import bitgouel.team.msg.global.error.DataErrorResponse
import bitgouel.team.msg.global.error.ErrorResponse
import bitgouel.team.msg.global.error.NoHandlerErrorResponse
import bitgouel.team.msg.global.error.ValidationErrorResponse
import bitgouel.team.msg.global.error.exception.BitgouelException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BitgouelException::class)
    fun bitgouelExceptionHandler(e: BitgouelException): ErrorResponse = ErrorResponse.of(e)

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ValidationErrorResponse = ErrorResponse.of(e)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ValidationErrorResponse = ErrorResponse.of(e)

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): DataErrorResponse = ErrorResponse.of(e)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): NoHandlerErrorResponse = ErrorResponse.of(e)

}