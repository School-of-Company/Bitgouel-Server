package team.msg.domain.eamil.mapper

import org.springframework.stereotype.Component
import team.msg.domain.eamil.presentation.data.request.SendEmailRequestData
import team.msg.domain.eamil.presentation.web.SendEmailWebRequest

@Component
class EmailRequestMapperImpl : EmailRequestMapper {
    override fun sendEmailWebRequestToDto(webRequest: SendEmailWebRequest): SendEmailRequestData =
        SendEmailRequestData(
            email = webRequest.email
        )
}