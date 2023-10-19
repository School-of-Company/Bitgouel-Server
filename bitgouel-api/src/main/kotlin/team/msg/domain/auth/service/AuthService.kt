package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.ProfessorSignUpRequest
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest

interface AuthService {
    fun studentSignUp(studentSignUpRequest: StudentSignUpRequest)
    fun teacherSignUp(teacherSignUpRequest: TeacherSignUpRequest)
    fun professorSignUp(professorSignUpRequest: ProfessorSignUpRequest)
}