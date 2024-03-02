package team.msg.domain.withdraw.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.student.model.Student
import team.msg.domain.withdraw.model.WithdrawStudent
import java.util.UUID

interface WithdrawStudentRepository : CrudRepository<WithdrawStudent, Long> {
    fun findByStudentIn(students: List<Student>): List<WithdrawStudent>
    fun deleteByStudent(student: Student)
}