package team.msg.domain.faq.presentation.data.response

import team.msg.domain.faq.model.Faq

data class FaqResponse(
    val id: Long,
    val question: String
) {
    companion object {
        fun listOf(faqs: List<Faq>): List<FaqResponse> = faqs.map {
            FaqResponse(
                id = it.id,
                question = it.question
            )
        }

        fun detailOf(faq: Faq) = FaqDetailsResponse(
            id = faq.id,
            question = faq.question,
            answer = faq.answer
        )
    }
}

data class AllFaqResponse(
    val faq: List<FaqResponse>
)

data class FaqDetailsResponse(
    val id: Long,
    val question: String,
    val answer: String
)