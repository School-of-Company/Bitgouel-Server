package team.msg.domain.eamil.presentation

import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.eamil.mapper.EmailRequestMapper
import team.msg.domain.eamil.presentation.web.SendEmailWebRequest
import team.msg.domain.eamil.service.EmailService

@RestController
@RequestMapping("/email")
class EmailController(
    private val emailRequestMapper: EmailRequestMapper,
    private val emailService: EmailService
) {
    @PostMapping
    fun sendEmail(@RequestBody @Valid webRequest: SendEmailWebRequest): ResponseEntity<Unit> {
        val request = emailRequestMapper.sendEmailWebRequestToDto(webRequest)
        emailService.sendEmail(request)
        return ResponseEntity.noContent().build()
    }
}