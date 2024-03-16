package team.msg.domain.eamil.mapper

import team.msg.domain.eamil.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.eamil.presentation.web.SendAuthenticationEmailWebRequest

interface EmailRequestMapper {
    fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequestData
}