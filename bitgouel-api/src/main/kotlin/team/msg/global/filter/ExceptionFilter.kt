package team.msg.global.filter

import team.msg.global.error.ErrorResponse
import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException
import team.msg.global.exception.InternalServerException
import com.fasterxml.jackson.databind.ObjectMapper
import team.msg.common.logger.LoggerDelegator
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

class ExceptionFilter: OncePerRequestFilter() {

    private val log by LoggerDelegator()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            filterChain.doFilter(request, response)
        }.onFailure { e ->
            when (e) {
                is BitgouelException -> {
                    log.error("Bitgouel Exception Occurred - Message = {}, Status = {}",
                        e.message, e.status)
                    sendError(response, ErrorResponse.of(e))
                }
                else -> {
                    log.error("Internal Exception Occurred - Message = {}, Status = {}",
                        e.message, GlobalErrorCode.INTERNAL_SERVER_ERROR.status)
                    sendError(response, ErrorResponse.of(InternalServerException("Internal Server Error")))
                }
            }
        }
    }

    private fun sendError(response: HttpServletResponse, errorResponse: ErrorResponse) {
        val responseString = ObjectMapper().writeValueAsString(errorResponse)
        response.status = errorResponse.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.writer.write(responseString)
    }

}