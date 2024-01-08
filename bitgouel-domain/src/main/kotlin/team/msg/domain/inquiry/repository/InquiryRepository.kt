package team.msg.domain.inquiry.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.user.model.User
import java.util.UUID

interface InquiryRepository : CrudRepository<Inquiry, UUID> {
    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUser(user: User): List<Inquiry>
}