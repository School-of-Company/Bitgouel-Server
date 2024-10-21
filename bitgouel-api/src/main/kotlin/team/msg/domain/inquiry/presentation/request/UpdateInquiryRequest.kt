package team.msg.domain.inquiry.presentation.request

import team.msg.domain.inquiry.model.Inquiry

data class UpdateInquiryRequest(
    val question: String,
    val questionDetail: String
) {
    fun update(inquiry: Inquiry): Inquiry = Inquiry(
        id = inquiry.id,
        ulid = inquiry.ulid,
        user = inquiry.user,
        question = question,
        questionDetail = questionDetail,
        answerStatus = inquiry.answerStatus
    )
}