package team.msg.domain.company.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.company.model.Company

interface CompanyRepository : CrudRepository<Company, Long> {
}