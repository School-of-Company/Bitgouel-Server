package team.msg.domain.faq.service

import team.msg.domain.faq.presentation.data.request.CreateFaqRequest

interface FaqService {
    fun createFaq(createFaqRequest: CreateFaqRequest)
}