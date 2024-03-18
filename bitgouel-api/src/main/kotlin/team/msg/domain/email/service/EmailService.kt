package team.msg.domain.email.service

import team.msg.domain.email.presentation.data.request.CheckEmailAuthenticationRequestData
import team.msg.domain.email.presentation.data.request.SendAuthenticationEmailRequestData
import team.msg.domain.email.presentation.data.response.CheckEmailAuthenticationResponseData

interface EmailService {
    fun sendAuthenticationEmail(request: SendAuthenticationEmailRequestData)
    fun emailAuthentication(email: String, code: String)
    fun checkEmailAuthentication(request: CheckEmailAuthenticationRequestData): CheckEmailAuthenticationResponseData
}