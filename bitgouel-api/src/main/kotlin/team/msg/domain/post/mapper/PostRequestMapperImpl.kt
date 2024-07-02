package team.msg.domain.post.mapper

import org.springframework.stereotype.Component
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.request.QueryAllPostsRequest
import team.msg.domain.post.presentation.data.request.UpdatePostRequest
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.presentation.web.QueryAllPostsWebRequest
import team.msg.domain.post.presentation.web.UpdatePostWebRequest

@Component
class PostRequestMapperImpl : PostRequestMapper {
    override fun createPostWebRequestToDto(webRequest: CreatePostWebRequest) = webRequest.run {
        CreatePostRequest(
            title = title,
            content = content,
            links = links,
            feedType = feedType
        )
    }

    override fun updatePostWebRequestToDto(webRequest: UpdatePostWebRequest) = webRequest.run {
        UpdatePostRequest(
            title = title,
            content = content,
            links = links
        )
    }

    override fun queryAllPostsWebRequestToDto(webRequest: QueryAllPostsWebRequest) = webRequest.run {
        QueryAllPostsRequest(
            postSequence = webRequest.postSequence,
            size = webRequest.size,
            type = webRequest.type
        )
    }
}