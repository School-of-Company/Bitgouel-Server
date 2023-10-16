package team.msg.domain.government.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.government.model.Government
import java.util.UUID

interface GovernmentRepository : CrudRepository<Government, UUID> {
    
}