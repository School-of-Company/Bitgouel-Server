package team.msg.domain.faq.presentation.data.response

import team.msg.domain.faq.model.Faq

data class FaqResponse(
    val id: Long,
    val question: String
) {
    companion object {
        fun of(faq: Faq) = FaqResponse(
            id = faq.id,
            question = faq.question
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
) {
    companion object {
        fun of(faq: Faq) = FaqDetailsResponse(
            id = faq.id,
            question = faq.question,
            answer = faq.answer
        )
    }
}