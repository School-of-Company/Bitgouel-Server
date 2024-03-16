package team.msg.domain.eamil.mapper

import team.msg.domain.eamil.presentation.data.request.SendEmailRequestData
import team.msg.domain.eamil.presentation.web.SendEmailWebRequest

interface EmailRequestMapper {
    fun sendEmailWebRequestToDto(webRequest: SendEmailWebRequest): SendEmailRequestData
}