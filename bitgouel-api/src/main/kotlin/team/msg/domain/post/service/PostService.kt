package team.msg.domain.post.service

import org.springframework.data.domain.Pageable
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.response.PostDetailsResponse
import team.msg.domain.post.presentation.data.response.PostsResponse
import java.util.UUID

interface PostService {
    fun createPost(request: CreatePostRequest)
    fun queryPosts(type: FeedType, pageable: Pageable): PostsResponse
    fun queryPostDetails(id: UUID): PostDetailsResponse
}