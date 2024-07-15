package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.msg.domain.club.model.QClub.club
import team.msg.domain.lecture.model.QLecture.lecture
import team.msg.domain.lecture.model.QRegisteredLecture.registeredLecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.lecture.repository.custom.projection.LectureAndRegisteredProjection
import team.msg.domain.lecture.repository.custom.projection.QLectureAndRegisteredProjection
import team.msg.domain.lecture.repository.custom.projection.QSignedUpStudentProjection
import team.msg.domain.lecture.repository.custom.projection.SignedUpStudentProjection
import team.msg.domain.student.model.QStudent.student
import java.util.*

@Component
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

    override fun findLecturesAndIsCompleteByStudentId(studentId: UUID): List<LectureAndRegisteredProjection> =
        queryFactory.select(
                QLectureAndRegisteredProjection(
                    lecture,
                    registeredLecture
                )
            ).from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .where(
                student.id.eq(studentId)
            )
            .fetch()

    override fun findSignedUpStudentsByLectureId(lectureId: UUID): List<SignedUpStudentProjection> =
        queryFactory.select(
                QSignedUpStudentProjection(
                    student,
                    registeredLecture
                )
            ).from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .where(
                lecture.id.eq(lectureId)
            )
            .fetch()

    override fun findSignedUpStudentsByLectureIdAndClubId(lectureId: UUID,clubId: Long): List<SignedUpStudentProjection> =
        queryFactory.select(
                QSignedUpStudentProjection(
                    student,
                    registeredLecture
                )
            ).from(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .leftJoin(student.club, club)
            .where(
                lecture.id.eq(lectureId),
                student.club.id.eq(clubId)
            )
            .fetch()

    override fun findByLectureIdAndStudentId(lectureId: UUID,studentId: UUID): RegisteredLecture? =
        queryFactory.selectFrom(registeredLecture)
            .leftJoin(registeredLecture.lecture, lecture)
            .leftJoin(registeredLecture.student, student)
            .where(
                lecture.id.eq(lectureId),
                student.id.eq(studentId)
            )
            .fetchOne()
}