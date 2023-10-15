package bitgouel.team.msg.global.security.handler

import bitgouel.team.msg.global.exception.InvalidRoleException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class CustomAccessDeniedHandler : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(this.javaClass.name)

    override fun handle(request: HttpServletRequest?,response: HttpServletResponse?,accessDeniedException: AccessDeniedException?) {
        log.warn("Access Denied")
        throw InvalidRoleException("검증되지 않은 권한입니다.")
    }
}