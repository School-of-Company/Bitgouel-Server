package team.msg.domain.inquiry.presentation.request

data class CreateInquiryRequest(
    val question: String,
    val questionDetail: String
)