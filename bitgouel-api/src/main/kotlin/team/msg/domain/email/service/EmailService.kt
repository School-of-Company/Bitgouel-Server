package team.msg.domain.email.service

import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequestData

interface EmailService {
    fun sendAuthenticationEmail(request: SendAuthenticationEmailRequestData)
    fun emailAuthentication(email: String, code: String)
}