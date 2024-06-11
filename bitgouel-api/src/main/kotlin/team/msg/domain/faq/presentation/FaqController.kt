package team.msg.domain.faq.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.faq.mapper.FaqRequestMapper
import team.msg.domain.faq.presentation.data.response.FaqsResponse
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest
import team.msg.domain.faq.service.FaqService

@RestController
@RequestMapping("/faq")
class FaqController(
    private val faqService: FaqService,
    private val faqRequestMapper: FaqRequestMapper
) {
    @PostMapping
    fun createFaq(@RequestBody @Valid webRequest: CreateFaqWebRequest): ResponseEntity<Unit> {
        val request = faqRequestMapper.createFaqWebRequestToDto(webRequest)
        faqService.createFaq(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryAllFaqs(): ResponseEntity<FaqsResponse> {
        val response = faqService.queryAllFaqs()
        return ResponseEntity.ok(response)
    }
}