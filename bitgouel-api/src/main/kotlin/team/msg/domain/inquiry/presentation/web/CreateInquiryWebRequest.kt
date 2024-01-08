package team.msg.domain.inquiry.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateInquiryWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val question: String,

    @field:NotBlank
    @field:Size(max=1000)
    val questionDetail: String
)
