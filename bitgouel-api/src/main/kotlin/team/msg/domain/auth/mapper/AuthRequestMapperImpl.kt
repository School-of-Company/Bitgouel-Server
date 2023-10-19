package team.msg.domain.auth.mapper

import org.springframework.stereotype.Component
import team.msg.domain.auth.presentation.data.request.ProfessorSignUpRequest
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest
import team.msg.domain.auth.presentation.data.web.ProfessorSignUpWebRequest
import team.msg.domain.auth.presentation.data.web.StudentSignUpWebRequest
import team.msg.domain.auth.presentation.data.web.TeacherSignUpWebRequest

@Component
class AuthRequestMapperImpl : AuthRequestMapper {

    /**
     * Student 회원가입 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
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

    /**
     * Teacher 회원가입 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun teacherSignUpWebRequestToDto(webRequest: TeacherSignUpWebRequest): TeacherSignUpRequest =
        TeacherSignUpRequest(
            email = webRequest.email,
            name = webRequest.name,
            password = webRequest.password,
            phoneNumber = webRequest.phoneNumber,
            highSchool = webRequest.highSchool,
            clubName = webRequest.clubName
        )

    /**
     * Professor 회원가입 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
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
}