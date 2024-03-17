package team.msg.domain.inquiry.repository.custom

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import team.msg.domain.inquiry.model.QInquiryAnswer
import java.util.*

@Repository
class InquiryAnswerRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : InquiryAnswerRepositoryCustom {
    override fun deleteAllByAdminId(adminId: UUID) {
        jpaQueryFactory.delete(QInquiryAnswer.inquiryAnswer)
            .where(QInquiryAnswer.inquiryAnswer.admin.id.eq(adminId))
            .execute()
    }
}