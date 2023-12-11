package team.msg.domain.post.mapper

import org.springframework.stereotype.Component
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.request.UpdatePostRequest
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.presentation.web.UpdatePostWebRequest

@Component
class PostRequestMapperImpl : PostRequestMapper {
    /**
     * post 생성 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createPostWebRequestToDto(webRequest: CreatePostWebRequest) = webRequest.run {
        CreatePostRequest(
            title = title,
            content = content,
            links = links.map { it.url },
            feedType = feedType
        )
    }

    /**
     * post 수정 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun updatePostWebRequestToDto(webRequest: UpdatePostWebRequest) = webRequest.run {
        UpdatePostRequest(
            title = title,
            content = content,
            links = links.map { it.url }
        )
    }
}