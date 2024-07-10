package team.msg.domain.university.service

import team.msg.domain.university.presentation.data.request.CreateDepartmentRequest
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.request.UpdateUniversityRequest
import team.msg.domain.university.presentation.data.response.UniversitiesResponse

interface UniversityService {
    fun createUniversity(request: CreateUniversityRequest)
    fun updateUniversity(id: Long, request: UpdateUniversityRequest)
    fun deleteUniversity(id: Long)
    fun queryUniversities(): UniversitiesResponse
    fun createDepartment(id: Long, request: CreateDepartmentRequest)
    fun deleteDepartment(id: Long, department: String)
}