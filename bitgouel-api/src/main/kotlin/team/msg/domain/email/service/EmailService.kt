package team.msg.domain.email.service

import team.msg.domain.email.presentation.data.request.CheckEmailAuthenticationRequest
import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequest
import team.msg.domain.email.presentation.data.response.CheckEmailAuthenticationResponse

interface EmailService {
    fun sendAuthenticationEmail(request: SendAuthenticationEmailRequest)
    fun emailAuthentication(email: String, code: String)
    fun checkEmailAuthentication(request: CheckEmailAuthenticationRequest): CheckEmailAuthenticationResponse
}