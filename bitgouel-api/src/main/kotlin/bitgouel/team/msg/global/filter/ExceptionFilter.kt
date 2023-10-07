package bitgouel.team.msg.global.filter

import bitgouel.team.msg.global.error.ErrorCode
import bitgouel.team.msg.global.error.ErrorResponse
import bitgouel.team.msg.global.error.exception.BitgouelException
import com.fasterxml.jackson.databind.ObjectMapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

class ExceptionFilter: OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            when (e) {
                is BitgouelException -> sendError(response, ErrorCode.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun sendError(response: HttpServletResponse, errorCode: ErrorCode) {
        val errorResponse = ErrorResponse(errorCode.message, errorCode.status)
        val responseString = ObjectMapper().writeValueAsString(errorResponse)
        response.status = errorCode.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.writer.write(responseString)
    }

}