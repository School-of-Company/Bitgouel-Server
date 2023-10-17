package team.msg.domain.auth.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.web.StudentSignUpWebRequest
import team.msg.domain.auth.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/student")
    fun studentSignUp(@RequestBody @Valid request: StudentSignUpWebRequest): ResponseEntity<Void> =
        authService.studentSignUp(StudentSignUpRequest(
            email = request.email,
            name = request.name,
            phoneNumber = request.phoneNumber,
            password = request.password,
            highSchool = request.highSchool,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number,
            admissionNumber = request.admissionNumber
        ))
            .let { ResponseEntity.status(HttpStatus.CREATED).build() }
}