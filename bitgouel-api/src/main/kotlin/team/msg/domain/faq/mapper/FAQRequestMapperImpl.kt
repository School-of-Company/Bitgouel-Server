package team.msg.domain.faq.mapper

import org.springframework.stereotype.Component
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest

@Component
class FaqRequestMapperImpl : FaqRequestMapper {

    override fun createFAQWebRequestToDto(createFAQWebRequest: CreateFaqWebRequest): CreateFaqRequest =
        CreateFaqRequest(
            question = createFAQWebRequest.question,
            answer = createFAQWebRequest.answer
        )

}