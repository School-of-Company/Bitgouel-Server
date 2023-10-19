package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.*

interface AuthService {
    fun studentSignUp(studentSignUpRequest: StudentSignUpRequest)
    fun teacherSignUp(teacherSignUpRequest: TeacherSignUpRequest)
    fun professorSignUp(professorSignUpRequest: ProfessorSignUpRequest)
    fun governmentSignUp(governmentSignUpRequest: GovernmentSignUpRequest)
    fun companyInstructorSignUp(companyInstructorSignUpRequest: CompanyInstructorSignUpRequest)
}