package team.msg.domain.certification.mapper

import org.springframework.stereotype.Component
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.presentation.data.web.UpdateCertificationWebRequest

@Component
class CertificationRequestMapperImpl : CertificationRequestMapper {
    /**
     * 자격증 등록 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createCertificationWebRequestToDto(webRequest: CreateCertificationWebRequest) = CreateCertificationRequest(
        name = webRequest.name,
        acquisitionDate = webRequest.acquisitionDate
    )

    /**
     * 자격증 수정 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun updateCertificationWebRequestToDto(webRequest: UpdateCertificationWebRequest) = UpdateCertificationRequest(
        name = webRequest.name,
        acquisitionDate = webRequest.acquisitionDate
    )

}