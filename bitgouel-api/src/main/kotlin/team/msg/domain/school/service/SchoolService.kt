package team.msg.domain.school.service

import org.springframework.web.multipart.MultipartFile
import team.msg.domain.school.presentation.data.request.CreateSchoolRequest
import team.msg.domain.school.presentation.data.request.UpdateSchoolRequest
import team.msg.domain.school.presentation.data.response.SchoolsResponse

interface SchoolService {
    fun querySchools(): SchoolsResponse
    fun createSchool(request: CreateSchoolRequest, logoImage: MultipartFile)
    fun updateSchool(id: Long, request: UpdateSchoolRequest)
    fun deleteSchool(id: Long)
}