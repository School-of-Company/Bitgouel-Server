package team.msg.domain.post.repository.custom

import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.repository.custom.projection.PostProjection
import java.util.*

interface CustomPostRepository {
    fun deleteAllByUserId(userId: UUID)
    fun findAll(postSequence: Int?, size: Int, feedType: FeedType?): List<PostProjection>
}