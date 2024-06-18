package team.msg.domain.company.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.company.model.Company
import java.util.UUID

interface CompanyRepository : CrudRepository<Company, UUID> {
}