package team.msg.domain.auth.mapper

import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.web.*

interface AuthRequestMapper {
    fun studentSignUpWebRequestToDto(webRequest: StudentSignUpWebRequest): StudentSignUpRequest
    fun teacherSignUpWebRequestToDto(webRequest: TeacherSignUpWebRequest): TeacherSignUpRequest
    fun professorSignUpWebRequestToDto(webRequest: ProfessorSignUpWebRequest): ProfessorSignUpRequest
    fun governmentSignUpWebRequestToDto(webRequest: GovernmentSignUpWebRequest): GovernmentSignUpRequest
    fun companyInstructorSignUpWebRequestToDto(webRequest: CompanyInstructorSignUpWebRequest): CompanyInstructorSignUpRequest
}