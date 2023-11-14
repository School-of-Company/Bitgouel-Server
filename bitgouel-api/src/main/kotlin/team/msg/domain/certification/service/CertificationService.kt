package team.msg.domain.certification.service

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationsResponse

interface CertificationService {
    fun createCertification(createCertificationRequest: CreateCertificationRequest)
    fun queryAllCertifications(): CertificationsResponse
}