package domain.company.repository

import domain.company.model.CompanyInstructor
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CompanyInstructorRepository : CrudRepository<CompanyInstructor, UUID> {
}