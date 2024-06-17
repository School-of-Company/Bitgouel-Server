package team.msg.domain.school.mapper

import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest

interface SchoolRequestMapper {
    fun createSchoolWebRequestToDto(webRequest: CreateSchoolWebRequest): CreateSchoolRequest
}