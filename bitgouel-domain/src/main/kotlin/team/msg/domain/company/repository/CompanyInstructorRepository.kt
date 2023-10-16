package team.msg.domain.company.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.company.model.CompanyInstructor
import java.util.UUID

interface CompanyInstructorRepository : CrudRepository<CompanyInstructor, UUID> {
}