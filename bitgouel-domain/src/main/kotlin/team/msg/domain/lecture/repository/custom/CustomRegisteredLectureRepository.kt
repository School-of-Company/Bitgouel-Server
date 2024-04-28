package team.msg.domain.lecture.repository.custom

import team.msg.domain.lecture.model.Lecture
import java.util.*

interface CustomRegisteredLectureRepository {
    fun deleteAllByStudentId(studentId: UUID)
    fun existsOne(studentId: UUID,lectureId: UUID): Boolean
    fun findLecturesAndIsCompleteByStudentId(studentId: UUID): List<Pair<Lecture, Boolean>>
}