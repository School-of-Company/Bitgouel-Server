package team.msg.domain.faq.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.faq.mapper.FaqRequestMapper
import team.msg.domain.faq.presentation.data.response.QueryAllFaqsResponse
import team.msg.domain.faq.presentation.data.response.QueryFaqDetailsResponse
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest
import team.msg.domain.faq.service.FaqService

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
    fun queryAllFaqs(): ResponseEntity<List<QueryAllFaqsResponse>> {
        val response = faqService.queryAllFaqs()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun queryFaqDetails(@PathVariable id: Long): ResponseEntity<QueryFaqDetailsResponse> {
        val response = faqService.queryFaqDetails(id)
        return ResponseEntity.ok(response)
    }
}