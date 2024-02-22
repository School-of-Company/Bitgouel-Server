package team.msg.domain.user.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.common.enums.ApproveStatus
import team.msg.domain.company.model.QCompanyInstructor.companyInstructor
import team.msg.domain.government.model.QGovernment.government
import team.msg.domain.professor.model.QProfessor.professor
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

    /**
     * User를 조회하는 쿼리입니다
     * 이름, 역할, 승인 상태등을 파라미터로 받아 조건을 지정합니다
     *
     * @param 검색할 유저의 이름에 포함되는 keyword, 유저의 역할을 나타내는 authority, 유저의 승인 여부를 나타내는 approveStatus
     * @return 검색 조건에 부합하는 user 리스트
     */
    override fun query(keyword: String, authority: Authority, approveStatus: ApproveStatus): List<User> =
        queryFactory
            .selectFrom(user)
            .where(
                nameLike(keyword),
                authorityEq(authority),
                approveStatusEq(approveStatus)
            )
            .orderBy(user.authority.asc(), user.name.asc())
            .fetch()

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

    /**
     * 강사를 조회하는 쿼리입니다.
     * keyword를 파라미터로 받아 조건을 지정합니다.
     * 유저의 이름과 소속 기관을 조회합니다.
     *
     * @param 검색할 유저의 이름과 기관에 포함되는 keyword
     * @return 검색 조건에 부합하는 user 리스트
     */
    override fun queryInstructorsAndOrganization(keyword: String): List<Pair<User, String>> =
        queryFactory.selectFrom(user)
            .leftJoin(professor.user,user)
            .leftJoin(companyInstructor.user,user)
            .leftJoin(government.user,user)
            .where(
                instructorNameLike(keyword),
                organizationNameLike(keyword)
            )
            .fetch()
            .map { user ->
                val organization = when {
                    professor.user.equals(user) -> professor.university.toString()
                    companyInstructor.user.equals(user) -> companyInstructor.company.toString()
                    government.user.equals(user) -> government.governmentName.toString()
                    else -> "소속 없음"
                }

                Pair(user, organization)
            }

    private fun instructorNameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null
        else professor.user.name.like("%$keyword%")
            .or(companyInstructor.user.name.like("%$keyword%"))
            .or(government.user.name.like("%$keyword%"))

    private fun organizationNameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null
        else professor.university.like("%$keyword")
            .or(companyInstructor.company.like("%$keyword"))
            .or(government.governmentName.like("%$keyword"))

    private fun nameLike(keyword: String): BooleanExpression? =
        if(keyword == "") null else user.name.like("%$keyword%")

    private fun authorityEq(authority: Authority): BooleanExpression? =
        if(authority == Authority.ROLE_USER) null else user.authority.eq(authority)

    private fun approveStatusEq(approveStatus: ApproveStatus): BooleanExpression =
        user.approveStatus.eq(approveStatus)
}