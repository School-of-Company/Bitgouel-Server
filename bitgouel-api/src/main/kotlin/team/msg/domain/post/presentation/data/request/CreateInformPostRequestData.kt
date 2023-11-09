package team.msg.domain.post.presentation.data.request

data class CreateInformPostRequestData(
    val title: String,
    val content: String,
    val link: String?
)