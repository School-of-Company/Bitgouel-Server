package team.msg.domain.certification.mapper

import org.springframework.stereotype.Component
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest

@Component
class CertificationRequestMapperImpl : CertificationRequestMapper {
    override fun createCertificationWebRequestToDto(webRequest: CreateCertificationWebRequest) = CreateCertificationRequest(
        name = webRequest.name,
        acquisitionDate = webRequest.acquisitionDate
    )

}