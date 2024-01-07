package team.msg.domain.inquiry.service

import team.msg.domain.inquiry.presentation.web.CreateInquiryRequest

interface InquiryService {
    fun createInquiry(request: CreateInquiryRequest)
}