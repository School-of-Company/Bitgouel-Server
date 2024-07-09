package team.msg.domain.university.mapper

import org.springframework.stereotype.Component
import team.msg.domain.university.presentation.data.request.CreateDepartmentRequest
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest

@Component
class UniversityRequestRequestMapperImpl : UniversityRequestMapper {

    override fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest) : CreateUniversityRequest =
        CreateUniversityRequest(
            universityName = webRequest.universityName
        )

    override fun createDepartmentWebRequestToDto(webRequest: CreateDepartmentWebRequest): CreateDepartmentRequest =
        CreateDepartmentRequest(
            department = webRequest.department
        )
}