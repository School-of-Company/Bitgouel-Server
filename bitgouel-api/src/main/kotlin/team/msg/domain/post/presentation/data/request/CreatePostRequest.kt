package team.msg.domain.post.presentation.data.request

import team.msg.domain.post.enums.FeedType

data class CreatePostRequest(
    val title: String,
    val content: String,
    val link: String?,
    val feedType: FeedType
)