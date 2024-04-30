package team.msg.domain.lecture.repository.custom

import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.student.model.Student
import java.util.*

interface CustomRegisteredLectureRepository {
    fun deleteAllByStudentId(studentId: UUID)
    fun existsOne(studentId: UUID,lectureId: UUID): Boolean
    fun findLecturesAndIsCompleteByStudentId(studentId: UUID): List<Pair<Lecture, Boolean>>
    fun findSignedUpStudentsByLectureId(lectureId: UUID): List<Student>
    fun findSignedUpStudentsByLectureIdAndClubId(lectureId: UUID, clubId: Long): List<Student>
    fun findByLectureIdAndStudentId(lectureId: UUID, studentId: UUID): RegisteredLecture?
}