package team.msg.domain.email.presentation

import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.email.mapper.EmailRequestMapper
import team.msg.domain.email.presentation.data.response.CheckEmailAuthenticationResponse
import team.msg.domain.email.presentation.web.SendAuthenticationEmailWebRequest
import team.msg.domain.email.service.EmailService

@RestController
@RequestMapping("/email")
class EmailController(
    private val emailRequestMapper: EmailRequestMapper,
    private val emailService: EmailService
) {
    @PostMapping
    fun sendAuthenticationEmail(@RequestBody @Valid webRequest: SendAuthenticationEmailWebRequest): ResponseEntity<Unit> {
        val request = emailRequestMapper.sendEmailWebRequestToDto(webRequest)
        emailService.sendAuthenticationEmail(request)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun checkEmailAuthentication(@RequestParam email: String): ResponseEntity<CheckEmailAuthenticationResponse> {
        val response = emailService.checkEmailAuthentication(email)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/authentication")
    fun emailAuthentication(@RequestParam email: String, @RequestParam code: String): ResponseEntity<String> {
        emailService.emailAuthentication(email, code)
        return ResponseEntity.ok("인증이 완료되었습니다.")
    }
}