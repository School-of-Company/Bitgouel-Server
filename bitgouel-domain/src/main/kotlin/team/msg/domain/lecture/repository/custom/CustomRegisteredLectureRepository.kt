package team.msg.domain.lecture.repository.custom

import java.util.*

interface CustomRegisteredLectureRepository {
    fun deleteAllByStudent(studentId: UUID)
}