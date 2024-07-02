package team.msg.domain.government.mapper

import org.springframework.stereotype.Component
import team.msg.domain.government.presentation.request.CreateGovernmentRequestData
import team.msg.domain.government.presentation.web.CreateGovernmentWebRequest

@Component
class GovernmentMapperImpl : GovernmentMapper {
    override fun createGovernmentWebRequestToDto(webRequest: CreateGovernmentWebRequest) = CreateGovernmentRequestData (
        field = webRequest.field,
        governmentName = webRequest.governmentName
    )
}