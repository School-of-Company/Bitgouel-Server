package team.msg.domain.faq.service

import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.data.response.AllFaqResponse
import team.msg.domain.faq.presentation.data.response.FaqDetailsResponse

interface FaqService {
    fun createFaq(createFaqRequest: CreateFaqRequest)
    fun queryAllFaqs(): AllFaqResponse
    fun queryFaqDetails(id: Long): FaqDetailsResponse
}