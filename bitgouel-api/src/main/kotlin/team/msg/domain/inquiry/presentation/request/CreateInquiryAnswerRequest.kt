package team.msg.domain.inquiry.presentation.request

data class CreateInquiryAnswerRequest(
    val question: String,
    val questionDetail: String
)
