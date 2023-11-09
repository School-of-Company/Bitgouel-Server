package team.msg.domain.post.presentation.data.request

data class CreatePostRequestData(
    val title: String,
    val content: String,
    val link: String?
)