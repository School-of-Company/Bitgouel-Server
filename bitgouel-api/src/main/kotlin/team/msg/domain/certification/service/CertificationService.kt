package team.msg.domain.certification.service

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest

interface CertificationService {
    fun createCertification(createCertificationRequest: CreateCertificationRequest)
}