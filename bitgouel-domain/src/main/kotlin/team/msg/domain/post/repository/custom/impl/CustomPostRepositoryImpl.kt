package team.msg.domain.post.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.model.Post
import team.msg.domain.post.model.QPost.post
import team.msg.domain.post.repository.custom.CustomPostRepository
import java.util.*

class CustomPostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomPostRepository {
    override fun deleteAllByUserId(userId: UUID) {
        queryFactory.delete(post)
            .where(post.userId.eq(userId))
            .execute()
    }

    override fun findAll(lastPostId: UUID, size: Long, feedType: FeedType): List<Post> =
        queryFactory.selectFrom(post)
            .where(isLastPostId(lastPostId))
            .where(post.feedType.eq(feedType))
            .orderBy(post.createdAt.desc())
            .limit(size)
            .fetch()

    private fun isLastPostId(postId: UUID): BooleanExpression? {
        if (postId == null)
            return null

        return post.id.lt(postId)
    }

}