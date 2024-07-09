package team.msg.domain.school.mapper

import org.springframework.stereotype.Component
import team.msg.domain.club.mapper.ClubRequestMapper
import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.request.UpdateSchoolRequest
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest
import team.msg.domain.school.presentation.web.UpdateSchoolWebRequest

@Component
class SchoolRequestMapperImpl : SchoolRequestMapper {

    override fun createSchoolWebRequestToDto(webRequest: CreateSchoolWebRequest): CreateSchoolRequest =
        CreateSchoolRequest(
            schoolName = webRequest.schoolName,
            line = webRequest.line,
            departments = webRequest.departments
        )

    override fun updateSchoolWebRequestToDto(webRequest: UpdateSchoolWebRequest): UpdateSchoolRequest =
        UpdateSchoolRequest(
            schoolName = webRequest.schoolName,
            line = webRequest.line,
            departments = webRequest.departments
        )

}