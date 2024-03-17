package team.msg.domain.eamil.presentation

import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.eamil.mapper.EmailRequestMapper
import team.msg.domain.eamil.presentation.web.SendAuthenticationEmailWebRequest
import team.msg.domain.eamil.service.EmailService

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

    @GetMapping("/authentication")
    fun emailAuthentication(@RequestParam email: String, @RequestParam code: String): ResponseEntity<Unit> {
        emailService.emailAuthentication(email, code)
        return ResponseEntity.noContent().build()
    }
}