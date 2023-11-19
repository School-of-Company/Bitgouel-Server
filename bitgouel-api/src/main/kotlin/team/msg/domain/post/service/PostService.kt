package team.msg.domain.post.service

import team.msg.domain.post.presentation.data.request.CreatePostRequest

interface PostService {
    fun createPost(request: CreatePostRequest)
}