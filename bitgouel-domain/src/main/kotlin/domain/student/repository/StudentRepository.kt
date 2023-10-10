package domain.student.repository

import domain.student.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentRepository : JpaRepository<Student, UUID> {
}