package team.msg.domain.faq.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateFaqWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val question: String,

    @field:NotBlank
    @field:Size(max=3000)
    val answer: String
)