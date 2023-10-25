package team.msg.domain.faq.presentation.data.request

data class CreateFaqRequest(
    val question: String,
    val answer: String
)