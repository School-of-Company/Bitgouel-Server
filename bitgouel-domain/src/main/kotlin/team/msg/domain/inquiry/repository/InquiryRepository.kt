package team.msg.domain.inquiry.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.inquiry.model.Inquiry
import java.util.UUID

interface InquiryRepository : CrudRepository<Inquiry, UUID> {
}