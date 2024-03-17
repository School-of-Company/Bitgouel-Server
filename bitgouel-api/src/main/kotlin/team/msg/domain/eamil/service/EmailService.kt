package team.msg.domain.eamil.service

import team.msg.domain.eamil.presentation.data.request.SendAuthenticationEmailRequestData

interface EmailService {
    fun sendAuthenticationEmail(request: SendAuthenticationEmailRequestData)
}