package team.msg.domain.auth.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
    fun studentSignUp(@RequestBody @Valid webRequest: StudentSignUpWebRequest): ResponseEntity<Void> {
        authService.studentSignUp(authRequestMapper.studentSignUpWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/teacher")
    fun teacherSignUp(@RequestBody @Valid webRequest: TeacherSignUpWebRequest): ResponseEntity<Void> {
        authService.teacherSignUp(authRequestMapper.teacherSignUpWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/professor")
    fun professorSignUp(@RequestBody @Valid webRequest: ProfessorSignUpWebRequest): ResponseEntity<Void> {
        authService.professorSignUp(authRequestMapper.professorSignUpWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/government")
    fun governmentSignUp(@RequestBody @Valid webRequest: GovernmentSignUpWebRequest): ResponseEntity<Void> {
        authService.governmentSignUp(authRequestMapper.governmentSignUpWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/company-instructor")
    fun companyInstructorSignUp(@RequestBody @Valid webRequest: CompanyInstructorSignUpWebRequest): ResponseEntity<Void> {
        authService.companyInstructorSignUp(authRequestMapper.companyInstructorSignUpWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid webRequest: LoginWebRequest): ResponseEntity<TokenResponse> {
        val response = authService.login(authRequestMapper.loginWebRequestToDto(webRequest))
        return ResponseEntity.ok(response)
    }

    @PatchMapping
    fun reissueToken(@RequestHeader("RefreshToken") refreshToken: String): ResponseEntity<TokenResponse> {
        val response = authService.reissueToken(refreshToken)
        return ResponseEntity.ok(response)
    }
}