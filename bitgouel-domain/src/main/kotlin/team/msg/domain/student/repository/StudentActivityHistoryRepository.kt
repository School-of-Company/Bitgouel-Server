package team.msg.domain.student.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivityHistory
import team.msg.domain.student.repository.custom.CustomStudentActivityHistoryRepository
import java.util.*

interface StudentActivityHistoryRepository : CrudRepository<StudentActivityHistory, UUID>, CustomStudentActivityHistoryRepository {
    fun findAllByStudent(student: Student): List<StudentActivityHistory>
}