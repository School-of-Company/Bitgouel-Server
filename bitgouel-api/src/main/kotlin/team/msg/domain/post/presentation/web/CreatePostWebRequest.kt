package team.msg.domain.post.presentation.web

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import team.msg.domain.post.enums.FeedType

data class CreatePostWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val title: String,

    @field:NotBlank
    @field:Size(max=2000)
    val content: String,

    @field:Valid
    val link: List<LinkWebRequest>,

    @field:NotNull
    val feedType: FeedType
){
    data class LinkWebRequest(
        @field:URL
        @field:NotBlank
        @field:Size(max=2083)
        val url: String
    )
}