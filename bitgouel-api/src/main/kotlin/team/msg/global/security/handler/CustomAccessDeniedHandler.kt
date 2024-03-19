package team.msg.global.security.handler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import team.msg.common.logger.LoggerDelegator
import team.msg.global.exception.InvalidRoleException

class CustomAccessDeniedHandler : AccessDeniedHandler {

    private val log by LoggerDelegator()

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {
        log.warn("========== Access Denied ==========")
        throw InvalidRoleException("검증되지 않은 권한입니다.")
    }
}