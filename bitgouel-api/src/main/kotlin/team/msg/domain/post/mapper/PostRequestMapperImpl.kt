package team.msg.domain.post.mapper

import org.springframework.stereotype.Component
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.data.request.CreatePostRequestData
import team.msg.domain.post.presentation.web.CreatePostWebRequest

@Component
class PostRequestMapperImpl : PostRequestMapper {
    override fun createPostWebRequestToDto(webRequest: CreatePostWebRequest, feedType: FeedType) = CreatePostRequestData(
        title = webRequest.title,
        content = webRequest.content,
        link = webRequest.link,
        feedType = feedType
    )
}