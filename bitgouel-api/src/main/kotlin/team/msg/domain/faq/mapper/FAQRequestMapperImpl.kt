package team.msg.domain.faq.mapper

import org.springframework.stereotype.Component
import team.msg.domain.faq.presentation.data.request.CreateFAQRequest
import team.msg.domain.faq.presentation.web.CreateFAQWebRequest

@Component
class FAQRequestMapperImpl : FAQRequestMapper {

    override fun createFAQWebRequestToDto(createFAQWebRequest: CreateFAQWebRequest): CreateFAQRequest =
        CreateFAQRequest(
            question = createFAQWebRequest.question,
            answer = createFAQWebRequest.answer
        )

}