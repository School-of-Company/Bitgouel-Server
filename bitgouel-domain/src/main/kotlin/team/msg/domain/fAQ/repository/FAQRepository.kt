package team.msg.domain.faq.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.faq.model.Faq
import java.util.*

interface FaqRepository : CrudRepository<Faq, UUID> {
}