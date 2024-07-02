package team.msg.domain.government.mapper

import team.msg.domain.government.presentation.request.CreateGovernmentRequestData
import team.msg.domain.government.presentation.web.CreateGovernmentWebRequest

interface GovernmentMapper {
    fun createGovernmentWebRequestToDto(webRequest: CreateGovernmentWebRequest): CreateGovernmentRequestData
}