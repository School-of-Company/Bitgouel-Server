package team.msg.domain.email.mapper

import team.msg.domain.email.presentation.data.request.CheckEmailAuthenticationRequestData
import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.email.presentation.web.CheckAuthenticationEmailWebRequest
import team.msg.domain.email.presentation.web.SendAuthenticationEmailWebRequest

interface EmailRequestMapper {
    fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequestData
    fun checkEmailWebRequestToDto(webRequest: CheckAuthenticationEmailWebRequest): CheckEmailAuthenticationRequestData
}