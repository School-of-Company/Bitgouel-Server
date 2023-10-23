package team.msg.domain.student.mapper

import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest
import team.msg.domain.student.presentation.data.web.UpdateStudentActivityWebRequest

interface StudentActivityMapper {
    fun createStudentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest
    fun updateStudentActivityWebRequestToDto(webRequest: UpdateStudentActivityWebRequest): UpdateStudentActivityRequest
}