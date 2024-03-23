package team.msg.domain.post.presentation.web

import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.post.enums.FeedType

data class QueryAllPostsWebRequest(
    @RequestParam(required = false)
    val postSequence: Int?,
    @RequestParam
    val size: Long,
    @RequestParam(required = false)
    val type: FeedType?
)