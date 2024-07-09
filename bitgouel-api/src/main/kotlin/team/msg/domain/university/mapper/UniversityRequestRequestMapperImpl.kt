package team.msg.domain.university.mapper

import org.springframework.stereotype.Component
import team.msg.domain.university.presentation.data.request.CreateDepartmentRequest
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.request.DeleteDepartmentRequest
import team.msg.domain.university.presentation.data.request.UpdateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
import team.msg.domain.university.presentation.data.web.DeleteDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.UpdateUniversityWebRequest

@Component
class UniversityRequestRequestMapperImpl : UniversityRequestMapper {

    override fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest) : CreateUniversityRequest =
        CreateUniversityRequest(
            universityName = webRequest.universityName
        )

    override fun updateUniversityWebRequestToDto(webRequest: UpdateUniversityWebRequest): UpdateUniversityRequest =
        UpdateUniversityRequest(
            universityName = webRequest.universityName
        )

    override fun createDepartmentWebRequestToDto(webRequest: CreateDepartmentWebRequest): CreateDepartmentRequest =
        CreateDepartmentRequest(
            department = webRequest.department
        )

    override fun deleteDepartmentWebRequestToDto(webRequest: DeleteDepartmentWebRequest): DeleteDepartmentRequest =
        DeleteDepartmentRequest(
            department = webRequest.department
        )
}