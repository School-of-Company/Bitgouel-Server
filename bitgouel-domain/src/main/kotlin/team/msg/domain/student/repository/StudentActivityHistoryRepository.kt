package team.msg.domain.student.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivityHistory
import java.util.UUID

interface StudentActivityHistoryRepository : CrudRepository<StudentActivityHistory, UUID> {
    fun findAllByStudent(student: Student): List<StudentActivityHistory>
}