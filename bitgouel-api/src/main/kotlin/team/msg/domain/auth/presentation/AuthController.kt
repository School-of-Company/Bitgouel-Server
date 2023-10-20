package team.msg.domain.auth.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.auth.mapper.AuthRequestMapper
import team.msg.domain.auth.presentation.data.response.TokenResponse
import team.msg.domain.auth.presentation.data.web.*
import team.msg.domain.auth.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val authRequestMapper: AuthRequestMapper
) {
    @PostMapping("/student")
    fun studentSignUp(@RequestBody @Valid request: StudentSignUpWebRequest): ResponseEntity<Void> {
        authService.studentSignUp(authRequestMapper.studentSignUpWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/teacher")
    fun teacherSignUp(@RequestBody @Valid request: TeacherSignUpWebRequest): ResponseEntity<Void> {
        authService.teacherSignUp(authRequestMapper.teacherSignUpWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/professor")
    fun professorSignUp(@RequestBody @Valid request: ProfessorSignUpWebRequest): ResponseEntity<Void> {
        authService.professorSignUp(authRequestMapper.professorSignUpWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/government")
    fun governmentSignUp(@RequestBody @Valid request: GovernmentSignUpWebRequest): ResponseEntity<Void> {
        authService.governmentSignUp(authRequestMapper.governmentSignUpWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/company-instructor")
    fun companyInstructorSignUp(@RequestBody @Valid request: CompanyInstructorSignUpWebRequest): ResponseEntity<Void> {
        authService.companyInstructorSignUp(authRequestMapper.companyInstructorSignUpWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginWebRequest): ResponseEntity<TokenResponse> {
        val response = authService.login(authRequestMapper.loginWebRequestToDto(request))
        return ResponseEntity.ok(response)
    }
}