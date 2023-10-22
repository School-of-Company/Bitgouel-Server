package team.msg.domain.auth.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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
        val request = authRequestMapper.studentSignUpWebRequestToDto(webRequest)
        authService.studentSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/teacher")
    fun teacherSignUp(@RequestBody @Valid webRequest: TeacherSignUpWebRequest): ResponseEntity<Void> {
        val request = authRequestMapper.teacherSignUpWebRequestToDto(webRequest)
        authService.teacherSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/professor")
    fun professorSignUp(@RequestBody @Valid webRequest: ProfessorSignUpWebRequest): ResponseEntity<Void> {
        val request = authRequestMapper.professorSignUpWebRequestToDto(webRequest)
        authService.professorSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/government")
    fun governmentSignUp(@RequestBody @Valid webRequest: GovernmentSignUpWebRequest): ResponseEntity<Void> {
        val request = authRequestMapper.governmentSignUpWebRequestToDto(webRequest)
        authService.governmentSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/company-instructor")
    fun companyInstructorSignUp(@RequestBody @Valid webRequest: CompanyInstructorSignUpWebRequest): ResponseEntity<Void> {
        val request = authRequestMapper.companyInstructorSignUpWebRequestToDto(webRequest)
        authService.companyInstructorSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid webRequest: LoginWebRequest): ResponseEntity<TokenResponse> {
        val request = authRequestMapper.loginWebRequestToDto(webRequest)
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @PatchMapping
    fun reissueToken(@RequestHeader("RefreshToken") refreshToken: String): ResponseEntity<TokenResponse> {
        val response = authService.reissueToken(refreshToken)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping
    fun logout(@RequestHeader("RefreshToken") refreshToken: String): ResponseEntity<Void> {
        authService.logout(refreshToken)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}