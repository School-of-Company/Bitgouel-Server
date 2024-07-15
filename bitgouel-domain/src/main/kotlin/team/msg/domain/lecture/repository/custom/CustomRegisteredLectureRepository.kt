package team.msg.domain.lecture.repository.custom

import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.repository.custom.projection.LectureAndRegisteredProjection
import team.msg.domain.lecture.repository.custom.projection.SignedUpStudentProjection
import java.util.*

interface CustomRegisteredLectureRepository {
    fun deleteAllByStudentId(studentId: UUID)
    fun existsOne(studentId: UUID,lectureId: UUID): Boolean
    fun findLecturesAndIsCompleteByStudentId(studentId: UUID): List<LectureAndRegisteredProjection>
    fun findSignedUpStudentsByLectureId(lectureId: UUID): List<SignedUpStudentProjection>
    fun findSignedUpStudentsByLectureIdAndClubId(lectureId: UUID, clubId: Long): List<SignedUpStudentProjection>
    fun findByLectureIdAndStudentId(lectureId: UUID, studentId: UUID): RegisteredLecture?
}