package team.msg.domain.inquiry.presentation.response

import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.model.InquiryAnswer
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class InquiryResponse(
    val id: UUID,
    val question: String,
    val userId: UUID,
    val username: String,
    val createdAt: LocalDate,
    val answerStatus: AnswerStatus
) {
    companion object {

        fun of(inquiry: Inquiry): InquiryResponse =
            InquiryResponse(
                id = inquiry.id,
                question = inquiry.question,
                userId = inquiry.user.id,
                username = inquiry.user.name,
                createdAt = inquiry.createdAt.toLocalDate(),
                answerStatus = inquiry.answerStatus
            )

        fun listOf(inquiries: List<Inquiry>): InquiryResponses = InquiryResponses(
            inquiries.map { of(it) }
        )

        fun detailOf(inquiry: Inquiry, inquiryAnswer: InquiryAnswer?) = InquiryDetailResponse(
            id = inquiry.id,
            question = inquiry.question,
            questionDetail = inquiry.questionDetail,
            questionerId = inquiry.user.id,
            questioner = inquiry.user.name,
            questionDate = inquiry.createdAt,
            answerStatus = inquiry.answerStatus,
            answer = inquiryAnswer?.answer,
            adminId = inquiryAnswer?.admin?.id,
            answeredDate = inquiryAnswer?.createdAt,
        )
    }
}

data class InquiryResponses(
    val inquiries: List<InquiryResponse>
)

data class InquiryDetailResponse(
    val id: UUID,
    val question: String,
    val questionDetail: String,
    val questionerId: UUID,
    val questioner: String,
    val questionDate: LocalDateTime,
    val answerStatus: AnswerStatus,
    val answer: String?,
    val adminId: UUID?,
    val answeredDate: LocalDateTime?
)