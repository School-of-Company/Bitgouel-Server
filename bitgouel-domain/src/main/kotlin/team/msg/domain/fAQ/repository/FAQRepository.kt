package team.msg.domain.fAQ.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.faq.model.Faq

interface FaqRepository : JpaRepository<Faq, Long> {
}