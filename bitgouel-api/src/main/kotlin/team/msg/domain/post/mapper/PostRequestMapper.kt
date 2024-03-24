package team.msg.domain.post.mapper

import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.request.QueryAllPostsRequest
import team.msg.domain.post.presentation.data.request.UpdatePostRequest
import team.msg.domain.post.presentation.web.CreatePostWebRequest
import team.msg.domain.post.presentation.web.QueryAllPostsWebRequest
import team.msg.domain.post.presentation.web.UpdatePostWebRequest

interface PostRequestMapper {
    fun createPostWebRequestToDto(webRequest: CreatePostWebRequest): CreatePostRequest
    fun updatePostWebRequestToDto(webRequest: UpdatePostWebRequest): UpdatePostRequest
    fun queryAllPostsWebRequestToDto(webRequest: QueryAllPostsWebRequest): QueryAllPostsRequest
}