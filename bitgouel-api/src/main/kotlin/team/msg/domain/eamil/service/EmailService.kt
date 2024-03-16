package team.msg.domain.eamil.service

import team.msg.domain.eamil.presentation.data.request.SendEmailRequestData

interface EmailService {
    fun sendEmail(request: SendEmailRequestData)
}