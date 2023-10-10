package domain.school.repository

import domain.school.model.School
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface SchoolRepository : CrudRepository<School, UUID> {
}