package team.msg.domain.certification.service

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import java.util.UUID

interface CertificationService {
    fun createCertification(studentId: UUID, createCertificationRequest: CreateCertificationRequest)
}