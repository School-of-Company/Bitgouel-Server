package domain.student.repository

import domain.student.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface StudentRepository : CrudRepository<Student, UUID> {
}