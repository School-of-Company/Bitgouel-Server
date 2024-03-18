package team.msg.domain.email.mapper

import team.msg.domain.email.presentation.data.request.CheckEmailAuthenticationRequest
import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequest
import team.msg.domain.email.presentation.web.CheckAuthenticationEmailWebRequest
import team.msg.domain.email.presentation.web.SendAuthenticationEmailWebRequest

interface EmailRequestMapper {
    fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequest
    fun checkEmailWebRequestToDto(webRequest: CheckAuthenticationEmailWebRequest): CheckEmailAuthenticationRequest
}