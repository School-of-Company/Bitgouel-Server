package team.msg.domain.inquiry.service

import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryResponses

interface InquiryService {
    fun createInquiry(request: CreateInquiryRequest)
    fun queryMyInquiries(): InquiryResponses
    fun queryAllInquiries(answerStatus: AnswerStatus?, keyword: String): InquiryResponses
}