package team.msg.domain.auth.mapper

import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.web.*

interface AuthRequestMapper {
    fun studentSignUpWebRequestToDto(webRequest: StudentSignUpWebRequest): StudentSignUpRequest
    fun teacherSignUpWebRequestToDto(webRequest: TeacherSignUpWebRequest): TeacherSignUpRequest
    fun bbozzakSignUpWebRequestToDto(webRequest: BbozzakSignUpWebRequest): BbozzakSignUpRequest
    fun professorSignUpWebRequestToDto(webRequest: ProfessorSignUpWebRequest): ProfessorSignUpRequest
    fun governmentSignUpWebRequestToDto(webRequest: GovernmentSignUpWebRequest): GovernmentSignUpRequest
    fun companyInstructorSignUpWebRequestToDto(webRequest: CompanyInstructorSignUpWebRequest): CompanyInstructorSignUpRequest
    fun loginWebRequestToDto(webRequest: LoginWebRequest): LoginRequest
}