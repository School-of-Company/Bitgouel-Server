package team.msg.domain.auth.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.auth.mapper.AuthRequestMapper
import team.msg.domain.auth.presentation.data.web.StudentSignUpWebRequest
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
}