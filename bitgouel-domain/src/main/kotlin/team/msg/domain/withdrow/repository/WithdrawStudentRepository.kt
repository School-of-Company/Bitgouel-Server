package team.msg.domain.withdrow.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.student.model.Student
import team.msg.domain.withdrow.model.WithdrawStudent

interface WithdrawStudentRepository : CrudRepository<WithdrawStudent, Long> {
    fun findByStudentIn(students: List<Student>): List<WithdrawStudent>
}