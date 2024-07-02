package team.msg.domain.auth.mapper

import org.springframework.stereotype.Component
import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.web.*

@Component
class AuthRequestMapperImpl : AuthRequestMapper {
    override fun studentSignUpWebRequestToDto(webRequest: StudentSignUpWebRequest): StudentSignUpRequest =
        StudentSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            phoneNumber = webRequest.phoneNumber,
            password = webRequest.password,
            highSchool = webRequest.highSchool,
            classRoom = webRequest.classRoom,
            grade = webRequest.grade,
            clubName = webRequest.clubName,
            number = webRequest.number,
            admissionNumber = webRequest.admissionNumber
        )

    override fun teacherSignUpWebRequestToDto(webRequest: TeacherSignUpWebRequest): TeacherSignUpRequest =
        TeacherSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            password = webRequest.password,
            phoneNumber = webRequest.phoneNumber,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName
        )

    override fun bbozzakSignUpWebRequestToDto(webRequest: BbozzakSignUpWebRequest): BbozzakSignUpRequest =
        BbozzakSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            phoneNumber = webRequest.phoneNumber,
            password = webRequest.password,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName
        )

    override fun professorSignUpWebRequestToDto(webRequest: ProfessorSignUpWebRequest): ProfessorSignUpRequest =
        ProfessorSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            phoneNumber = webRequest.phoneNumber,
            password = webRequest.password,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName,
            university = webRequest.university
        )

    override fun governmentSignUpWebRequestToDto(webRequest: GovernmentSignUpWebRequest): GovernmentSignUpRequest =
        GovernmentSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            phoneNumber = webRequest.phoneNumber,
            password = webRequest.password,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName,
            governmentName = webRequest.governmentName,
            position = webRequest.position,
            sectors = webRequest.sectors
        )

    override fun companyInstructorSignUpWebRequestToDto(webRequest: CompanyInstructorSignUpWebRequest): CompanyInstructorSignUpRequest =
        CompanyInstructorSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            phoneNumber = webRequest.phoneNumber,
            password = webRequest.password,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName,
            companyName = webRequest.company
        )

    override fun loginWebRequestToDto(webRequest: LoginWebRequest): LoginRequest =
        LoginRequest(
            email = webRequest.email,
            password = webRequest.password
        )

    override fun changePasswordWebRequestToDto(webRequest: ChangePasswordWebRequest): ChangePasswordRequest =
        ChangePasswordRequest(
            email = webRequest.email,
            newPassword = webRequest.newPassword
        )
}