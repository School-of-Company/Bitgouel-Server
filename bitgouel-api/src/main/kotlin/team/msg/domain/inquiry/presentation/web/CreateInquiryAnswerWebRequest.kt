package team.msg.domain.inquiry.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateInquiryAnswerWebRequest(
    @field:NotBlank
    @field:Size(max = 500)
    val answer: String
)
