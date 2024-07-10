package team.msg.domain.university.mapper

import team.msg.domain.university.presentation.data.request.CreateDepartmentRequest
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.request.UpdateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
import team.msg.domain.university.presentation.data.web.UpdateUniversityWebRequest

interface UniversityRequestMapper {
    fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest): CreateUniversityRequest
    fun updateUniversityWebRequestToDto(webRequest: UpdateUniversityWebRequest): UpdateUniversityRequest
    fun createDepartmentWebRequestToDto(webRequest: CreateDepartmentWebRequest): CreateDepartmentRequest
}
