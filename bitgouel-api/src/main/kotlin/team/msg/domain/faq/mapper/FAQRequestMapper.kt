package team.msg.domain.faq.mapper

import team.msg.domain.faq.presentation.data.request.CreateFAQRequest
import team.msg.domain.faq.presentation.web.CreateFAQWebRequest

interface FAQRequestMapper {
    fun createFAQWebRequestToDto(createFAQWebRequest: CreateFAQWebRequest): CreateFAQRequest
}