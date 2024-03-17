package team.msg.domain.inquiry.repository.custom

import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry
import java.util.UUID

interface CustomInquiryRepository {
    fun deleteAllByUserId(userId: UUID)
    fun search(answerStatus: AnswerStatus?, keyword: String): List<Inquiry>
}