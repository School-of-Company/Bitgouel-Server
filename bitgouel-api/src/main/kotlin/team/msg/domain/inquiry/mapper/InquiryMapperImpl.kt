package team.msg.domain.inquiry.mapper

import org.springframework.stereotype.Component
import team.msg.domain.inquiry.presentation.request.CreateInquiryAnswerRequest
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.request.UpdateInquiryRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryAnswerWebRequest
import team.msg.domain.inquiry.presentation.web.CreateInquiryWebRequest
import team.msg.domain.inquiry.presentation.web.UpdateInquiryWebRequest

@Component
class InquiryMapperImpl : InquiryMapper {

    override fun createInquiryWebRequestToDto(webRequest: CreateInquiryWebRequest): CreateInquiryRequest =
        CreateInquiryRequest(
            question = webRequest.question,
            questionDetail = webRequest.questionDetail
        )

    override fun updateInquiryWebRequestToDto(webRequest: UpdateInquiryWebRequest): UpdateInquiryRequest =
        UpdateInquiryRequest(
            question = webRequest.question,
            questionDetail = webRequest.questionDetail
        )
    override fun createInquiryAnswerWebRequestToDto(webRequest: CreateInquiryAnswerWebRequest): CreateInquiryAnswerRequest =
        CreateInquiryAnswerRequest(
            answer = webRequest.answer
        )

}
