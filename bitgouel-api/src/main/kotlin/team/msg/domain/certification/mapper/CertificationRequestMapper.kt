package team.msg.domain.certification.mapper

import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.presentation.data.web.UpdateCertificationWebRequest

interface CertificationRequestMapper {
    fun createCertificationWebRequestToDto(webRequest: CreateCertificationWebRequest): CreateCertificationRequest
    fun updateCertificationWebRequestToDto(webRequest: UpdateCertificationWebRequest): UpdateCertificationRequest
}