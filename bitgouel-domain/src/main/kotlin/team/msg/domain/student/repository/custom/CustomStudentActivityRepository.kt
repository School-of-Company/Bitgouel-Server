package team.msg.domain.student.repository.custom

import team.msg.domain.student.model.Student

interface CustomStudentActivityRepository {
    fun deleteAllByStudent(student: Student)
}