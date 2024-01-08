package team.msg.domain.inquiry.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.mapper.InquiryMapper
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest
import team.msg.domain.inquiry.service.InquiryService

@RestController
@RequestMapping("/inquiry")
class InquiryController(
    private val inquiryService: InquiryService,
    private val inquiryMapper: InquiryMapper
) {

    @PostMapping
    fun createInquiry(@Valid @RequestBody webRequest: CreateInquiryWebRequest): ResponseEntity<Void> {
        val request = inquiryMapper.createInquiryWebRequestToDto(webRequest)
        inquiryService.createInquiry(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryMyInquiries(): ResponseEntity<InquiryResponses> {
        val response = inquiryService.queryMyInquiries()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/all")
    fun queryAllInquires(@RequestParam answerStatus: AnswerStatus? = null, @RequestParam keyword: String = ""): ResponseEntity<InquiryResponses> {

    }
}
