package team.msg.domain.post.repository.custom

import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.model.Post
import java.util.UUID

interface CustomPostRepository {
    fun deleteAllByUserId(userId: UUID)
    fun findAll(postSequence: Int?, size: Long, feedType: FeedType?): List<Post>
}