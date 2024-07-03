package team.msg.domain.university.mapper

import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest

interface UniversityRequestMapper {
    fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest): CreateUniversityRequest
}
