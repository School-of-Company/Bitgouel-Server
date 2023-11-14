package team.msg.domain.post.presentation.web

import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import org.hibernate.validator.constraints.URL

data class CreatePostWebRequest(
    @field:NotBlank
    @Max(100)
    val title: String,

    @field:NotBlank
    @Max(2000)
    val content: String,

    @field:NotNull
    @field:Valid
    val link: List<Link>
){
    data class Link(
        @field:URL
        @field:NotBlank
        val url: String
    )
}