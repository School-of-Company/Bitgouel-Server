package team.msg.domain.post.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.post.model.Post
import team.msg.domain.post.presentation.data.request.CreatePostRequestData
import team.msg.domain.post.repository.PostRepository
import java.util.UUID

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userUtil: UserUtil
) : PostService {
    @Transactional(rollbackFor = [Exception::class])
    override fun createPostService(request: CreatePostRequestData) {
        val user = userUtil.queryCurrentUser()

        val post = Post(
            id = UUID.randomUUID(),
            title = request.title,
            content = request.content,
            link = request.link,
            feedType = request.feedType,
            userId = user.id
        )

        postRepository.save(post)
    }
}