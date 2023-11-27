package team.msg.domain.student.repository

import team.msg.domain.student.model.Student

interface CustomStudentActivityRepository {
    fun deleteAllByStudent(student: Student)
}