package team.msg.domain.post.presentation.web

import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank

data class CreateInformPostWebRequest(
    @field:NotBlank
    @Max(100)
    val title: String,

    @field:NotBlank
    @Max(500)
    val content: String,

    @Max(2083)
    val link: String?
)