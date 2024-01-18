package team.msg.domain.inquiry.presentation.web

import team.msg.domain.inquiry.enums.AnswerStatus

data class QueryAllInquiresRequest(
    val answerStatus: AnswerStatus?,
    val keyword: String
)
