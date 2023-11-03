package team.msg.domain.student.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import team.msg.domain.student.model.Student
import team.msg.domain.user.model.User
import java.util.*

interface StudentRepository : CrudRepository<Student, UUID> {
    fun findByUser(user: User): Student?
    @Query("SELECT student FROM Student student JOIN FETCH student.user WHERE student.id = :id")
    fun findStudentById(id: UUID): Student?
}
