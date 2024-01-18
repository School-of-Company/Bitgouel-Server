package team.msg.domain.inquiry.service

import team.msg.domain.inquiry.presentation.request.CreateInquiryAnswerRequest
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.request.UpdateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryDetailResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.presentation.web.QueryAllInquiresRequest
import java.util.UUID

interface InquiryService {
    fun createInquiry(request: CreateInquiryRequest)
    fun queryMyInquiries(): InquiryResponses
    fun queryAllInquiries(request: QueryAllInquiresRequest): InquiryResponses
    fun queryInquiryDetail(id: UUID): InquiryDetailResponse
    fun deleteInquiry(id: UUID)
    fun rejectInquiry(id: UUID)
    fun updateInquiry(id: UUID, request: UpdateInquiryRequest)
    fun replyInquiry(id: UUID, request: CreateInquiryAnswerRequest)
}