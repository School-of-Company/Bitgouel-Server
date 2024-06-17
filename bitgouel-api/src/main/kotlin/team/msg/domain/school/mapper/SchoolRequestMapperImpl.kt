package team.msg.domain.school.mapper

import org.springframework.stereotype.Component
import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest

@Component
class SchoolRequestMapperImpl : SchoolRequestMapper {

    override fun createSchoolWebRequestToDto(webRequest: CreateSchoolWebRequest): CreateSchoolRequest =
        CreateSchoolRequest(
            schoolName = webRequest.schoolName
        )
}