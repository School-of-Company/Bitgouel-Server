package team.msg.domain.user.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.QUser.user
import team.msg.domain.user.model.User
import java.util.Objects.isNull

class CustomUserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomUserRepository {
    override fun query(keyword: String, authority: Authority, pageable: Pageable): Page<User> {
        val users = queryFactory
            .selectFrom(user)
            .where(
                nameLike(keyword),
                authorityEq(authority)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(user.name.asc())
            .fetch()

        return PageImpl(users)
    }

    private fun nameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null else user.name.like(keyword)

    private fun authorityEq(authority: Authority): BooleanExpression? =
        if(authority == Authority.ROLE_USER) null else user.authority.eq(authority)
}