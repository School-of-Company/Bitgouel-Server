package team.msg.domain.lecture.repository.custom

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.QLecture.lecture

@Component
class LectureRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : LectureRepositoryCustom {
    override fun findAllByApproveStatusAndLectureType(pageable: Pageable, approveStatus: ApproveStatus?, lectureType: LectureType?): Page<Lecture> {
        val response = queryFactory
            .selectFrom(lecture)
            .where(
                eqLectureType(lectureType),
                eqApproveStatus(approveStatus)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(response)
    }

    private fun eqLectureType(lectureType: LectureType?): BooleanExpression? {
        lectureType ?: return null
        return lecture.lectureType.eq(lectureType)
    }

    private fun eqApproveStatus(approveStatus: ApproveStatus?): BooleanExpression? {
        approveStatus ?: return null
        return lecture.approveStatus.eq(approveStatus)
    }
}