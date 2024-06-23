package team.msg.domain.school.service

import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.request.UpdateSchoolRequest
import team.msg.domain.school.presentation.data.response.SchoolsResponse

interface SchoolService {
    fun querySchools(): SchoolsResponse
    fun createSchool(request: CreateSchoolRequest)
    fun updateSchool(id: Long, request: UpdateSchoolRequest)
    fun deleteSchool(id: Long)
}