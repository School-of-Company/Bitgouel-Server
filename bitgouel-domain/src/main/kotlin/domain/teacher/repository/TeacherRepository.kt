package domain.teacher.repository

import domain.teacher.model.Teacher
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface TeacherRepository : CrudRepository<Teacher, UUID> {
}