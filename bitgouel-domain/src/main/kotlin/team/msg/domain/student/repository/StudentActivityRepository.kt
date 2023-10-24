package team.msg.domain.student.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.teacher.model.Teacher
import java.util.*

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID> {
    fun findByIdAndStudent(id: UUID, student: Student): StudentActivity?
    fun findByIdAndTeacher(id: UUID, teacher: Teacher): StudentActivity?
}