package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import team.msg.domain.lecture.model.QLecture.lecture
import team.msg.domain.lecture.model.QRegisteredLecture.registeredLecture
import team.msg.domain.lecture.repository.custom.CustomLectureRepository
import team.msg.domain.lecture.repository.custom.projection.LectureListProjection
import team.msg.domain.lecture.repository.custom.projection.QLectureListProjection
import java.util.*
import java.util.Objects.isNull

@Component
class CustomLectureRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomLectureRepository {
    override fun findAllByLectureType(pageable: Pageable, lectureType: String?): Page<LectureListProjection> {
        val content = queryFactory.select(
            QLectureListProjection(
                lecture,
                registeredLecture.count()
            )
        ).from(lecture)
            .leftJoin(registeredLecture).on(lecture.eq(registeredLecture.lecture))
            .where(
                eqLectureType(lectureType),
                eqIsDeleted(false)
            )
            .groupBy(lecture)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(lecture.count())
            .from(lecture)
            .where(
                eqLectureType(lectureType),
                eqIsDeleted(false)
            ).fetchOne()!!

        return PageImpl(content, pageable, count)
    }

    override fun deleteAllByUserId(userId: UUID) {
        queryFactory.update(lecture)
            .set(lecture.isDeleted, true)
            .where(lecture.user.id.eq(userId))
            .execute()
    }

    override fun findAllLineByDivision(division: String, keyword: String?): List<String> =
        queryFactory
            .select(lecture.line)
            .from(lecture)
            .where(
                lecture.division.contains(division),
                eqLine(keyword),
                eqIsDeleted(false)
            )
            .fetch()
            .distinct()

    override fun findAllDepartment(keyword: String?): List<String> =
        queryFactory
            .select(lecture.department)
            .from(lecture)
            .where(
                eqDepartment(keyword),
                eqIsDeleted(false)
            )
            .fetch()
            .distinct()

    override fun findAllDivisions(keyword: String?): List<String> =
        queryFactory
            .select(lecture.division)
            .from(lecture)
            .where(
                eqDivision(keyword),
                eqIsDeleted(false)
            )
            .fetch()
            .distinct()

    private fun eqLectureType(lectureType: String?): BooleanExpression? =
        if(isNull(lectureType)) null else lecture.lectureType.contains(lectureType)

    private fun eqLine(keyword: String?): BooleanExpression? =
        if(keyword.isNullOrBlank()) null else lecture.line.contains(keyword)

    private fun eqDepartment(keyword: String?): BooleanExpression? =
        if(keyword.isNullOrBlank()) null else lecture.department.contains(keyword)

    private fun eqDivision(keyword: String?): BooleanExpression? =
        if(keyword.isNullOrBlank()) null else lecture.division.contains(keyword)

    private fun eqIsDeleted(isDeleted: Boolean): BooleanExpression =
         lecture.isDeleted.eq(isDeleted)
}