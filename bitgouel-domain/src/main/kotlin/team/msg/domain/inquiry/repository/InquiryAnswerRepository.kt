package team.msg.domain.inquiry.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.inquiry.model.InquiryAnswer
import team.msg.domain.inquiry.repository.custom.CustomInquiryAnswerRepository
import java.util.UUID

interface InquiryAnswerRepository : CrudRepository<InquiryAnswer, UUID>, CustomInquiryAnswerRepository {
    @EntityGraph(attributePaths = ["admin"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByInquiryId(inquiryId: UUID): InquiryAnswer?
    fun existsByInquiryId(inquiryId: UUID): Boolean
}