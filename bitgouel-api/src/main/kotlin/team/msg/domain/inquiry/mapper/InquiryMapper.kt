package team.msg.domain.inquiry.mapper

import team.msg.domain.inquiry.presentation.request.CreateInquiryAnswerRequest
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.request.UpdateInquiryRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryAnswerWebRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest
import team.msg.domain.inquiry.presentation.web.UpdateInquiryWebRequest

interface InquiryMapper {
    fun createInquiryWebRequestToDto(webRequest: CreateInquiryWebRequest): CreateInquiryRequest
    fun updateInquiryWebRequestToDto(webRequest: UpdateInquiryWebRequest): UpdateInquiryRequest
    fun createInquiryAnswerWebRequestToDto(webRequest: CreateInquiryAnswerWebRequest): CreateInquiryAnswerRequest
}
