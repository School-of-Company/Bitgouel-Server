package team.msg.domain.student.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import java.util.*

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID> {
    fun findByIdAndStudent(id: UUID, student: Student): StudentActivity?
    fun findAllByStudent(student: Student): List<StudentActivity>
}