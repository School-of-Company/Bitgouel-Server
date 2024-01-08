package team.msg.domain.inquiry.repository.custom

import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry

interface InquiryRepositoryCustom {
    fun search(answerStatus: AnswerStatus?, keyword: String): List<Inquiry>
}