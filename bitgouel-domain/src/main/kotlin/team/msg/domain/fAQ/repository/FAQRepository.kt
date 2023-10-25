package team.msg.domain.fAQ.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.fAQ.model.FAQ
import java.util.*

interface FAQRepository : CrudRepository<FAQ, UUID> {
}