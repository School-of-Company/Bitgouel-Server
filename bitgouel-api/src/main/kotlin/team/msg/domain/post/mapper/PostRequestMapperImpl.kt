package team.msg.domain.post.mapper

import org.springframework.stereotype.Component
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.web.CreatePostWebRequest

@Component
class PostRequestMapperImpl : PostRequestMapper {
    /**
     * post 생성 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createPostWebRequestToDto(webRequest: CreatePostWebRequest) = CreatePostRequest(
        title = webRequest.title,
        content = webRequest.content,
        link = webRequest.link?.map { it.url },
        feedType = webRequest.feedType
    )
}