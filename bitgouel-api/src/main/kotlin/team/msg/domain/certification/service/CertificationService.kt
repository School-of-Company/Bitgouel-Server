package team.msg.domain.certification.service

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import java.util.UUID

interface CertificationService {
    fun createCertification(createCertificationRequest: CreateCertificationRequest)
    fun queryCertifications(): CertificationsResponse
    fun queryCertifications(studentId: UUID): CertificationsResponse
}