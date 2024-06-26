package team.msg.domain.post.presentation.web

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.web.validation.URLList

data class CreatePostWebRequest(
    @field:NotBlank
    @field:Size(max=100)
    val title: String,

    @field:NotBlank
    @field:Size(max=5000)
    val content: String,

    @field:Valid
    @field:URLList
    val links: List<String>,

    @field:NotNull
    val feedType: FeedType
)