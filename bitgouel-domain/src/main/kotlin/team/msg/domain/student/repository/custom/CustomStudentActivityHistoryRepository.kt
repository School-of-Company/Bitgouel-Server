package team.msg.domain.student.repository.custom

import team.msg.domain.student.model.Student

interface CustomStudentActivityHistoryRepository {
    fun deleteAllByStudent(student: Student)
}