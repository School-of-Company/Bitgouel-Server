package team.msg.domain.student.repository.custom

import java.util.*

interface CustomStudentActivityHistoryRepository {
    fun deleteAllByStudent(studentId: UUID)
}