package team.msg.domain.post.mapper

import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.web.CreatePostWebRequest

interface PostRequestMapper {
    fun createPostWebRequestToDto(webRequest: CreatePostWebRequest): CreatePostRequest
}