package team.msg.domain.school.mapper

import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.request.UpdateSchoolRequest
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest
import team.msg.domain.school.presentation.web.UpdateSchoolWebRequest

interface SchoolRequestMapper {
    fun createSchoolWebRequestToDto(webRequest: CreateSchoolWebRequest): CreateSchoolRequest
    fun updateSchoolWebRequestToDto(webRequest: UpdateSchoolWebRequest): UpdateSchoolRequest
}