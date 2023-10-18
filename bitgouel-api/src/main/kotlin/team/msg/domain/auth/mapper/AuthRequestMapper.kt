package team.msg.domain.auth.mapper

import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.web.StudentSignUpWebRequest

interface AuthRequestMapper {
    fun studentSignUpWebRequestToDto(webRequest: StudentSignUpWebRequest): StudentSignUpRequest
}