package team.msg.domain.user.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.QUser.user
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.custom.CustomUserRepository

class CustomUserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomUserRepository {
    override fun query(keyword: String, authority: Authority, pageable: Pageable): Page<User> {

        /**
         * User를 authority, name 순으로 정렬 및 페이징하여 조회하는 쿼리입니다
         */
        val users = queryFactory
            .selectFrom(user)
            .where(
                nameLike(keyword),
                authorityEq(authority)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(user.authority.asc(), user.name.asc())
            .fetch()

        /**
         * 검색 조건에 맞게 조회된 행의 개수를 조회하는 쿼리입니다
         */
        val countQuery = queryFactory
            .select(user.count())
            .where(
                nameLike(keyword),
                authorityEq(authority)
            )
            .from(user)

        return PageableExecutionUtils.getPage(users, pageable) { countQuery.fetchOne()!! }
    }

    private fun nameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null else user.name.like(keyword)

    private fun authorityEq(authority: Authority): BooleanExpression? =
        if(authority == Authority.ROLE_USER) null else user.authority.eq(authority)
}