package team.msg.domain.inquiry.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.inquiry.model.InquiryAnswer
import java.util.UUID

interface InquiryAnswerRepository : CrudRepository<InquiryAnswer, UUID> {
}