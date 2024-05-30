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
    fun studentSignUp(request: StudentSignUpRequest)
    fun teacherSignUp(request: TeacherSignUpRequest)
    fun bbozzakSignUp(request: BbozzakSignUpRequest)
    fun professorSignUp(request: ProfessorSignUpRequest)
    fun governmentSignUp(request: GovernmentSignUpRequest)
    fun companyInstructorSignUp(request: CompanyInstructorSignUpRequest)
    fun login(request: LoginRequest): TokenResponse
    fun reissueToken(requestToken: String): TokenResponse
    fun logout(requestToken: String)
    fun changePassword(request: ChangePasswordRequest)
    fun withdraw()
}