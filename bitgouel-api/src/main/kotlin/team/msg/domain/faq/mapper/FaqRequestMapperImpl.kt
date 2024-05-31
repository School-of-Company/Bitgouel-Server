package team.msg.domain.faq.mapper

import org.springframework.stereotype.Component
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.web.CreateFaqWebRequest

@Component
class FaqRequestMapperImpl : FaqRequestMapper {

    /**
     * FAQ 등록 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createFaqWebRequestToDto(webRequest: CreateFaqWebRequest): CreateFaqRequest =
        CreateFaqRequest(
            question = webRequest.question,
            answer = webRequest.answer
        )

}