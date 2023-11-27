package team.msg.domain.post.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
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
}