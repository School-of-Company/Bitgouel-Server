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

    override fun findAll(postSequence: Int?, size: Long, feedType: FeedType?): List<Post> =
        queryFactory.selectFrom(post)
            .where(isLastPostId(postSequence))
            .where(feedTypeEq(feedType))
            .orderBy(post.createdAt.asc())
            .limit(size)
            .fetch()

    private fun isLastPostId(postSequence: Int?): BooleanExpression? =
        if (postSequence == null) null else post.postSequence.gt(postSequence)

    private fun feedTypeEq(feedType: FeedType?): BooleanExpression? =
        if (feedType == null) null else post.feedType.eq(feedType)

}