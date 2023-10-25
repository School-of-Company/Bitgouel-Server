package team.msg.domain.faq.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.faq.model.Faq

interface FaqRepository : CrudRepository<Faq, Long> {
}