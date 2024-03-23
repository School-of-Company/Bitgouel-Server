package team.msg.domain.post.service

import org.springframework.data.domain.Pageable
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.presentation.data.request.CreatePostRequest
import team.msg.domain.post.presentation.data.request.QueryAllPostsRequest
import team.msg.domain.post.presentation.data.request.UpdatePostRequest
import team.msg.domain.post.presentation.data.response.PagingPostsResponse
import team.msg.domain.post.presentation.data.response.PostDetailsResponse
import team.msg.domain.post.presentation.data.response.PostsResponse
import java.util.*

interface PostService {
    fun createPost(request: CreatePostRequest)
    fun updatePost(id: UUID, request: UpdatePostRequest)
    fun deletePost(id: UUID)
    fun queryPosts(type: FeedType, pageable: Pageable): PagingPostsResponse
    fun queryPosts(request: QueryAllPostsRequest): PostsResponse
    fun queryPostDetails(id: UUID): PostDetailsResponse
}