package team.msg.domain.post.presentation.data.request

import team.msg.domain.post.enums.FeedType

class QueryAllPostsRequest(
    val postSequence: Int?,
    val size: Long,
    val type: FeedType?
)