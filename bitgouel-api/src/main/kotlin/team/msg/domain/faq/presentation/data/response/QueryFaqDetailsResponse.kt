package team.msg.domain.faq.presentation.data.response

data class QueryFaqDetailsResponse(
    val id: Long,
    val question: String,
    val answer: String
)