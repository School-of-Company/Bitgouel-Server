package team.msg.domain.auth.service

import team.msg.domain.auth.presentation.data.request.BbozzakSignUpRequest
import team.msg.domain.auth.presentation.data.request.ChangePasswordRequest
import team.msg.domain.auth.presentation.data.request.CompanyInstructorSignUpRequest
import team.msg.domain.auth.presentation.data.request.GovernmentSignUpRequest
import team.msg.domain.auth.presentation.data.request.LoginRequest
import team.msg.domain.auth.presentation.data.request.ProfessorSignUpRequest
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest
import team.msg.domain.auth.presentation.data.response.TokenResponse

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
    fun changePassword(changePasswordRequest: ChangePasswordRequest)
    fun withdraw()
}