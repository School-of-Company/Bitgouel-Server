package team.msg.domain.post.service

import org.springframework.data.domain.Pageable
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.response.PostsResponse

interface PostService {
    fun createPost(request: CreatePostRequest)
    fun queryPosts(type: FeedType, pageable: Pageable): PostsResponse
}