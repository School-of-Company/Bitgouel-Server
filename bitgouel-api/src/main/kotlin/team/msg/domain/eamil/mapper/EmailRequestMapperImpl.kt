package team.msg.domain.eamil.mapper

import org.springframework.stereotype.Component
import team.msg.domain.eamil.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.eamil.presentation.web.SendAuthenticationEmailWebRequest

@Component
class EmailRequestMapperImpl : EmailRequestMapper {
    override fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequestData =
        SendAuthenticationEmailRequestData(
            email = webRequest.email
        )
}