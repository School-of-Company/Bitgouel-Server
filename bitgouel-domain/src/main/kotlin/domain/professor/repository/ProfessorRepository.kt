package domain.professor.repository

import domain.professor.model.Professor
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ProfessorRepository : CrudRepository<Professor, UUID> {
}