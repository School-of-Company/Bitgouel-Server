package team.msg.global.security.handler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import team.msg.global.exception.ForbiddenException

class CustomAuthenticationEntryPointHandler : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(this.javaClass.name)

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        log.warn("========== AuthenticationEntryPoint ==========")
        throw ForbiddenException("Forbidden")
    }
}