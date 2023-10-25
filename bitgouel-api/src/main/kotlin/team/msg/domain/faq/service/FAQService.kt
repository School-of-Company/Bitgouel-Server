package team.msg.domain.faq.service

import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.data.response.QueryAllFaqsResponse

interface FaqService {
    fun createFaq(createFaqRequest: CreateFaqRequest)
    fun queryAllFaqs(): List<QueryAllFaqsResponse>
}