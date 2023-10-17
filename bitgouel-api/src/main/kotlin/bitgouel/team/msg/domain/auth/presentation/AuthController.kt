package bitgouel.team.msg.domain.auth.presentation

import bitgouel.team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import bitgouel.team.msg.domain.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/student")
    fun studentSignUp(@RequestBody studentSignUpRequest: StudentSignUpRequest): ResponseEntity<Void> =
        authService.studentSignUp(studentSignUpRequest)
            .let { ResponseEntity.status(HttpStatus.CREATED).build() }
}