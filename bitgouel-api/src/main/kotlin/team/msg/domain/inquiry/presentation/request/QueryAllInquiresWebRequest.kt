package team.msg.domain.inquiry.presentation.request

import javax.persistence.EnumType
import javax.persistence.Enumerated
import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.inquiry.enums.AnswerStatus

data class QueryAllInquiresWebRequest(
    @RequestParam
    @Enumerated(EnumType.STRING)
    val answerStatus: AnswerStatus?,

    @RequestParam
    val keyword: String?
)
