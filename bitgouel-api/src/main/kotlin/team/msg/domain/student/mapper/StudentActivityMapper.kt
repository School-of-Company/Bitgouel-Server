package team.msg.domain.student.mapper

import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest

interface StudentActivityMapper {
    fun createStudentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest
}