package team.msg.domain.inquiry.presentation.web

import javax.validation.constraints.NotBlank

data class CreateInquiryWebRequest(
    @field:NotBlank
    val question: String
)
