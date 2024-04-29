package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.club.model.QClub.club
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.QLecture.lecture
import team.msg.domain.lecture.model.QRegisteredLecture.registeredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.student.model.QStudent.student
import team.msg.domain.student.model.Student
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

    override fun findLecturesAndIsCompleteByStudentId(studentId: UUID): List<Pair<Lecture, Boolean>> {
        val fetch = queryFactory.select(lecture, registeredLecture.isComplete)
            .from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .where(
                student.id.eq(studentId)
            )
            .fetch()

        return fetch.map {
            Pair(
                it[lecture]!!,
                it[registeredLecture.isComplete]!!
            )
        }

    }

    override fun findSignedUpStudentsByLectureId(lectureId: UUID): List<Student> =
        queryFactory.select(student)
            .from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .where(
                lecture.id.eq(lectureId)
            )
            .fetch()

    override fun findSignedUpStudentsByLectureIdAndClubId(lectureId: UUID,clubId: Long): List<Student> =
        queryFactory.select(student)
            .from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .leftJoin(student.club, club)
            .where(
                lecture.id.eq(lectureId),
                student.club.id.eq(clubId)
            )
            .fetch()

}