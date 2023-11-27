package team.msg.domain.student.repository

import team.msg.domain.student.model.Student

interface CustomStudentActivityHistoryRepository {
    fun deleteAllByStudent(student: Student)
}