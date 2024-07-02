package team.msg.domain.email.mapper

import org.springframework.stereotype.Component
import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequest
import team.msg.domain.email.presentation.web.SendAuthenticationEmailWebRequest

@Component
class EmailRequestMapperImpl : EmailRequestMapper {
    override fun sendEmailWebRequestToDto(webRequest: SendAuthenticationEmailWebRequest): SendAuthenticationEmailRequest =
        SendAuthenticationEmailRequest(
            email = webRequest.email
        )
}