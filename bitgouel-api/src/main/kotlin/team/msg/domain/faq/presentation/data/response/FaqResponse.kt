package team.msg.domain.faq.presentation.data.response

import team.msg.domain.faq.model.Faq

data class FaqResponse(
    val id: Long,
    val question: String,
    val answer: String
) {
    companion object {
        fun listOf(faqs: List<Faq>): List<FaqResponse> = faqs.map {
            FaqResponse(
                id = it.id,
                question = it.question,
                answer = it.answer
            )
        }
    }
}

data class FaqsResponse(
    val faqs: List<FaqResponse>
)