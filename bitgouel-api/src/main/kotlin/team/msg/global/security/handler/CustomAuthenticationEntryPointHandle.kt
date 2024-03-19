package team.msg.global.security.handler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import team.msg.common.logger.LoggerDelegator
import team.msg.global.exception.ForbiddenException

class CustomAuthenticationEntryPointHandler : AuthenticationEntryPoint {

    private val log by LoggerDelegator()

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        log.warn("========== AuthenticationEntryPoint ==========")
        throw ForbiddenException("Forbidden")
    }
}