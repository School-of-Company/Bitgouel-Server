package team.msg.domain.user.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.QUser.user
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.custom.CustomUserRepository
import team.msg.domain.user.repository.custom.projection.QUserNameProjectionData
import team.msg.domain.user.repository.custom.projection.UserNameProjectionData
import java.util.*

class CustomUserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomUserRepository {
    override fun query(keyword: String, authority: Authority): Page<User> {
        /**
         * User를 authority, name 순으로 정렬 및 페이징하여 조회하는 쿼리입니다
         */
        val users = queryFactory
            .selectFrom(user)
            .where(
                nameLike(keyword),
                authorityEq(authority)
            )
            .orderBy(user.authority.asc(), user.name.asc())
            .fetch()
    }

    /**
     * 요청된 유저 아이디에 따라 조회된 유저의 이름을 반환합니다.
     * 조회된 유저가 없다면 null을 반환합니다.
     *
     * @param 이름을 조회할 유저의 id
     * @return 유저의 이름
     */
    override fun queryNameById(id: UUID): UserNameProjectionData? = queryFactory
            .select(QUserNameProjectionData(user.name))
            .where(user.id.eq(id))
            .from(user)
            .fetchOne()

    private fun nameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null else user.name.like("%$keyword%")

    private fun authorityEq(authority: Authority): BooleanExpression? =
        if(authority == Authority.ROLE_USER) null else user.authority.eq(authority)
}