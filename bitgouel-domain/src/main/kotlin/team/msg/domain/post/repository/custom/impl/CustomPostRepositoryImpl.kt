package team.msg.domain.post.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.model.QPost.post
import team.msg.domain.post.repository.custom.CustomPostRepository
import team.msg.domain.post.repository.custom.projection.PostProjection
import team.msg.domain.post.repository.custom.projection.QPostProjection
import java.util.*

class CustomPostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomPostRepository {
    override fun deleteAllByUserId(userId: UUID) {
        queryFactory.delete(post)
            .where(post.userId.eq(userId))
            .execute()
    }

    override fun findAll(postSequence: Int?, size: Long, feedType: FeedType?): List<PostProjection> =
        queryFactory
            .select(
                QPostProjection(
                    post.id,
                    post.title,
                    post.modifiedAt,
                    post.postSequence
                )
            )
            .from(post)
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