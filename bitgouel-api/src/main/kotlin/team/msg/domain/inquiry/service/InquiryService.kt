package team.msg.domain.inquiry.service

import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest

interface InquiryService {
    fun createInquiry(request: CreateInquiryRequest)
}