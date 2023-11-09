package team.msg.domain.post.service

import team.msg.domain.post.presentation.data.request.CreatePostRequestData

interface PostService {
    fun createPostService(request: CreatePostRequestData)
}