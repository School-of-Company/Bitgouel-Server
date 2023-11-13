package team.msg.domain.faq.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.faq.mapper.FaqRequestMapper
import team.msg.domain.faq.presentation.data.response.AllFaqResponse
import team.msg.domain.faq.presentation.data.response.FaqDetailsResponse
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest
import team.msg.domain.faq.service.FaqService
import javax.validation.Valid

@RestController
@RequestMapping("/faq")
class FaqController(
    private val faqService: FaqService,
    private val faqRequestMapper: FaqRequestMapper
) {
    @PostMapping
    fun createFaq(@RequestBody @Valid webRequest: CreateFaqWebRequest): ResponseEntity<Void> {
        val request = faqRequestMapper.createFaqWebRequestToDto(webRequest)
        faqService.createFaq(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryAllFaqs(): ResponseEntity<AllFaqResponse> {
        val response = faqService.queryAllFaqs()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun queryFaqDetails(@PathVariable id: Long): ResponseEntity<FaqDetailsResponse> {
        val response = faqService.queryFaqDetails(id)
        return ResponseEntity.ok(response)
    }
}