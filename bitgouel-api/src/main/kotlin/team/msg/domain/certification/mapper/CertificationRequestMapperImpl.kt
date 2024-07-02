package team.msg.domain.certification.mapper

import org.springframework.stereotype.Component
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.presentation.data.web.UpdateCertificationWebRequest

@Component
class CertificationRequestMapperImpl : CertificationRequestMapper {
    override fun createCertificationWebRequestToDto(webRequest: CreateCertificationWebRequest) = CreateCertificationRequest(
        name = webRequest.name,
        acquisitionDate = webRequest.acquisitionDate
    )

    override fun updateCertificationWebRequestToDto(webRequest: UpdateCertificationWebRequest) = UpdateCertificationRequest(
        name = webRequest.name,
        acquisitionDate = webRequest.acquisitionDate
    )

}