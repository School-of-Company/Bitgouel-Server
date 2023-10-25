package team.msg.domain.faq.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.faq.mapper.FAQRequestMapper
import team.msg.domain.faq.presentation.web.CreateFAQWebRequest
import team.msg.domain.faq.service.FAQService

@RestController
@RequestMapping("/faq")
class FAQController(
    private val faqService: FAQService,
    private val faqRequestMapper: FAQRequestMapper
) {
    @PostMapping
    fun createFAQ(webRequest: CreateFAQWebRequest): ResponseEntity<Void> {
        val request = faqRequestMapper.createFAQWebRequestToDto(webRequest)
        faqService.createFAQ(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}