package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest

interface AuthService {
    fun studentSignUp(studentSignUpRequest: StudentSignUpRequest)
}