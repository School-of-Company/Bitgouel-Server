package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.QLecture.lecture
import team.msg.domain.lecture.model.QRegisteredLecture.registeredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.student.model.QStudent.student
import team.msg.domain.user.model.QUser
import java.util.*

class CustomRegisteredLectureRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomRegisteredLectureRepository {
    override fun deleteAllByStudentId(studentId: UUID) {
        queryFactory.delete(registeredLecture)
            .where(registeredLecture.student.id.eq(studentId))
            .execute()
    }

    override fun existsOne(studentId: UUID, lectureId: UUID): Boolean {
        val fetchOne = queryFactory.selectOne()
            .from(registeredLecture)
            .where(
                student.id.eq(studentId),
                lecture.id.eq(lectureId)
            )
            .fetchFirst()

        return fetchOne != null
    }

    override fun findLecturesByStudentId(studentId: UUID): List<Lecture> =
        queryFactory.select(lecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .from(registeredLecture)
            .where(
                student.id.eq(studentId)
            )
            .fetch()
}