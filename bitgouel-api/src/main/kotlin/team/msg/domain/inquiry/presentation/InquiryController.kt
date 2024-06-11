package team.msg.domain.inquiry.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.inquiry.mapper.InquiryMapper
import team.msg.domain.inquiry.presentation.response.InquiryDetailResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.presentation.web.CreateInquiryAnswerWebRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest
import team.msg.domain.inquiry.presentation.request.QueryAllInquiresWebRequest
import team.msg.domain.inquiry.presentation.web.UpdateInquiryWebRequest
import team.msg.domain.inquiry.service.InquiryService
import java.util.UUID

@RestController
@RequestMapping("/inquiry")
class InquiryController(
    private val inquiryService: InquiryService,
    private val inquiryMapper: InquiryMapper
) {

    @PostMapping
    fun createInquiry(@Valid @RequestBody webRequest: CreateInquiryWebRequest): ResponseEntity<Unit> {
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
    fun queryAllInquires(webRequest: QueryAllInquiresWebRequest): ResponseEntity<InquiryResponses> {
        val request = inquiryMapper.queryAllInquiresWebRequestToDto(webRequest)
        val response = inquiryService.queryAllInquiries(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun queryInquiryDetail(@PathVariable id: UUID): ResponseEntity<InquiryDetailResponse> {
        val response = inquiryService.queryInquiryDetail(id)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteInquiry(@PathVariable id: UUID): ResponseEntity<Unit> {
        inquiryService.deleteInquiry(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}/reject")
    fun rejectInquiry(@PathVariable id: UUID): ResponseEntity<Unit> {
        inquiryService.rejectInquiry(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    fun updateInquiry(@PathVariable id: UUID, @Valid @RequestBody webRequest: UpdateInquiryWebRequest): ResponseEntity<Unit> {
        val request = inquiryMapper.updateInquiryWebRequestToDto(webRequest)
        inquiryService.updateInquiry(id, request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/answer")
    fun replyInquiry(@PathVariable id: UUID, @Valid @RequestBody webRequest: CreateInquiryAnswerWebRequest): ResponseEntity<Unit> {
        val request = inquiryMapper.createInquiryAnswerWebRequestToDto(webRequest)
        inquiryService.replyInquiry(id, request)
        return ResponseEntity.noContent().build()
    }
}
