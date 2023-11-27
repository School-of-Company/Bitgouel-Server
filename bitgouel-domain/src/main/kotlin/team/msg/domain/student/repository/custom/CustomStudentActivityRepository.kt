package team.msg.domain.student.repository.custom

import java.util.*

interface CustomStudentActivityRepository {
    fun deleteAllByStudentId(studentId: UUID)
}