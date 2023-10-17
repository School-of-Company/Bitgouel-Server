package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.StudentSignUpWebRequest

interface AuthService {
    fun studentSignUp(studentSignUpWebRequest: StudentSignUpWebRequest)
}