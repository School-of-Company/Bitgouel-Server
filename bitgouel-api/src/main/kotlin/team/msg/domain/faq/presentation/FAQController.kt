package team.msg.domain.faq.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.faq.mapper.FaqRequestMapper
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest
import team.msg.domain.faq.service.FaqService

@RestController
@RequestMapping("/FAQ")
class FaqController(
    private val faqService: FaqService,
    private val faqRequestMapper: FaqRequestMapper
) {
    @PostMapping
    fun createFAQ(@RequestBody @Valid webRequest: CreateFaqWebRequest): ResponseEntity<Void> {
        val request = faqRequestMapper.createFAQWebRequestToDto(webRequest)
        faqService.createFAQ(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}