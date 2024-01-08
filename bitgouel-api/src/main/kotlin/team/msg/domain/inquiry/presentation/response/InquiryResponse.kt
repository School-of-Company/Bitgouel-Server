package team.msg.domain.inquiry.presentation.response

import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry
import java.time.LocalDateTime
import java.util.*

data class InquiryResponse(
    val id: UUID,
    val question: String,
    val userId: UUID,
    val username: String,
    val createdAt: LocalDateTime,
    val answerStatus: AnswerStatus
) {
    companion object {

        fun of(inquiry: Inquiry): InquiryResponse =
            InquiryResponse(
                id = inquiry.id,
                question = inquiry.question,
                userId = inquiry.user.id,
                username = inquiry.user.name,
                createdAt = inquiry.createdAt,
                answerStatus = inquiry.answerStatus
            )

        fun listOf(inquiries: List<Inquiry>): InquiryResponses = InquiryResponses(
            inquiries.map { of(it) }
        )
    }
}

data class InquiryResponses(
    val inquiries: List<InquiryResponse>
)