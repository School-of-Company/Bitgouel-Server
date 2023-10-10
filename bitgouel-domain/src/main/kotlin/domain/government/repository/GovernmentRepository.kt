package domain.government.repository

import domain.government.model.Government
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface GovernmentRepository : CrudRepository<Government, UUID> {
    
}