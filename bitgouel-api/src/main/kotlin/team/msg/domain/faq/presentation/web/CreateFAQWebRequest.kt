package team.msg.domain.faq.presentation.web

import javax.validation.constraints.NotBlank

data class CreateFAQWebRequest(
    @field:NotBlank
    val question: String,

    @field:NotBlank
    val answer: String
)