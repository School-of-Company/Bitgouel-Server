package team.msg.domain.faq.service

import team.msg.domain.faq.presentation.data.request.CreateFAQRequest

interface FAQService {
    fun createFAQ(createFAQRequest: CreateFAQRequest)
}