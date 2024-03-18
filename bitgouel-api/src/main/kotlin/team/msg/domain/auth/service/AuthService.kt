package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.response.TokenResponse
import team.msg.domain.auth.presentation.data.request.BbozzakSignUpRequest
import team.msg.domain.auth.presentation.data.web.ChangePasswordWebRequest

interface AuthService {
    fun studentSignUp(studentSignUpRequest: StudentSignUpRequest)
    fun teacherSignUp(teacherSignUpRequest: TeacherSignUpRequest)
    fun bbozzakSignUp(bbozzakSignUpRequest: BbozzakSignUpRequest)
    fun professorSignUp(professorSignUpRequest: ProfessorSignUpRequest)
    fun governmentSignUp(governmentSignUpRequest: GovernmentSignUpRequest)
    fun companyInstructorSignUp(companyInstructorSignUpRequest: CompanyInstructorSignUpRequest)
    fun login(request: LoginRequest): TokenResponse
    fun reissueToken(refreshToken: String): TokenResponse
    fun logout(refreshToken: String)
    fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordWebRequest
    fun withdraw()
}