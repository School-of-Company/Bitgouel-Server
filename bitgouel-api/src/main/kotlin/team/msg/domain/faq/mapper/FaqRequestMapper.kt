package team.msg.domain.faq.mapper

import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest

interface FaqRequestMapper {
    fun createFaqWebRequestToDto(createFaqWebRequest: CreateFaqWebRequest): CreateFaqRequest
}