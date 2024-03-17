package team.msg.domain.inquiry.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import team.msg.domain.inquiry.model.QInquiryAnswer
import team.msg.domain.inquiry.repository.custom.CustomInquiryAnswerRepository
import java.util.*

@Repository
class CustomInquiryAnswerRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomInquiryAnswerRepository {
    override fun deleteAllByAdminId(adminId: UUID) {
        jpaQueryFactory.delete(QInquiryAnswer.inquiryAnswer)
            .where(QInquiryAnswer.inquiryAnswer.admin.id.eq(adminId))
            .execute()
    }
}