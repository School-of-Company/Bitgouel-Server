package team.msg.domain.post.presentation.web

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL

data class UpdatePostWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val title: String,

    @field:NotBlank
    @field:Size(max=2000)
    val content: String,

    @field:Valid
    val link: List<LinkWebRequest>?
){
    data class LinkWebRequest(
        @field:URL
        @field:NotBlank
        @field:Size(max=2083)
        val url: String
    )
}