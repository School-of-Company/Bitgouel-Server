package team.msg.domain.school.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.school.model.QSchool.school
import team.msg.domain.school.repository.custom.CustomSchoolRepository

class CustomSchoolRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomSchoolRepository {

    override fun existsOne(id: Long): Boolean {
        val fetchOne = queryFactory.selectOne()
            .from(school)
            .where(school.id.eq(id))
            .fetchFirst() // limit 1

        return fetchOne != null
    }
}