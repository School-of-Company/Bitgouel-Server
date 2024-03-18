package team.msg.domain.email.mapper

import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.email.presentation.web.SendAuthenticationEmailWebRequest

interface EmailRequestMapper {
    fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequestData
}