package team.msg.domain.inquiry.mapper

import org.springframework.stereotype.Component
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest

@Component
class InquiryMapperImpl : InquiryMapper {

    override fun createInquiryWebRequestToDto(webRequest: CreateInquiryWebRequest): CreateInquiryRequest =
        CreateInquiryRequest(
            question = webRequest.question,
            questionDetail = webRequest.questionDetail
        )

}
