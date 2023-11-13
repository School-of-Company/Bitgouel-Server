package team.msg.domain.certification.mapper

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest

interface CertificationRequestMapper {
    fun createCertificationWebRequestToDto(createCertificationWebRequest: CreateCertificationWebRequest): CreateCertificationRequest
}