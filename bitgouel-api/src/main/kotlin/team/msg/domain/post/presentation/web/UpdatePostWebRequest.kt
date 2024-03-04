package team.msg.domain.post.presentation.web

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import team.msg.domain.post.presentation.web.validation.URLList

data class UpdatePostWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val title: String,

    @field:NotBlank
    @field:Size(max=2000)
    val content: String,

    @field:Valid
    @field:URLList
    val links: List<String>
)