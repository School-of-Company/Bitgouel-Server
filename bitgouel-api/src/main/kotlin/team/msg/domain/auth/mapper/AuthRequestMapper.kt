package team.msg.domain.auth.mapper

import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest
import team.msg.domain.auth.presentation.data.web.StudentSignUpWebRequest
import team.msg.domain.auth.presentation.data.web.TeacherSignUpWebRequest

interface AuthRequestMapper {
    fun studentSignUpWebRequestToDto(webRequest: StudentSignUpWebRequest): StudentSignUpRequest
    fun teacherSignUpWebRequestToDto(webRequest: TeacherSignUpWebRequest): TeacherSignUpRequest
}