package team.msg.domain.club.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.club.model.QClub.club

class CustomClubRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomClubRepository {

    override fun existsOne(id: Long): Boolean {
        val fetchOne = queryFactory.selectOne()
            .from(club)
            .where(club.id.eq(id))
            .fetchFirst() // limit 1

        return fetchOne != null
    }
}