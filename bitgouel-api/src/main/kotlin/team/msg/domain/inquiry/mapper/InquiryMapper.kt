package team.msg.domain.inquiry.mapper

import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest

interface InquiryMapper {
    fun createInquiryWebRequestToDto(webRequest: CreateInquiryWebRequest): CreateInquiryRequest
}
