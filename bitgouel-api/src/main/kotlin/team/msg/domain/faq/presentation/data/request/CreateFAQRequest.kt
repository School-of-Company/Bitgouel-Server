package team.msg.domain.faq.presentation.data.request

data class CreateFAQRequest(
    val question: String,
    val answer: String
)