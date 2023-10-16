package team.msg.domain.professor.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.professor.model.Professor
import java.util.UUID

interface ProfessorRepository : CrudRepository<Professor, UUID> {
}