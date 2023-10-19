package team.msg.domain.user.mapper

import team.msg.domain.user.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.user.presentation.data.web.CreateStudentActivityWebRequest

interface StudentActivityMapper {
    fun studentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest
}