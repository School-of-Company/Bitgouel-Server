package team.msg.domain.inquiry.repository.custom.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.model.QInquiry
import team.msg.domain.inquiry.model.QInquiry.inquiry
import team.msg.domain.inquiry.repository.custom.CustomInquiryRepository
import java.util.*

@Repository
class CustomInquiryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomInquiryRepository {
    override fun deleteAllByUserId(userId: UUID) {
        jpaQueryFactory.delete(QInquiry.inquiry)
            .where(QInquiry.inquiry.user.id.eq(userId))
            .execute()
    }

    override fun search(answerStatus: AnswerStatus?, keyword: String): List<Inquiry> {
        return jpaQueryFactory.selectFrom(inquiry)
            .where(
                answerStatusEq(answerStatus),
                keywordLike(keyword)
            ).fetch()
    }

    private fun answerStatusEq(answerStatus: AnswerStatus?): BooleanExpression? =
        if(answerStatus == null) null else inquiry.answerStatus.eq(answerStatus)

    private fun keywordLike(keyword: String): BooleanExpression? =
        if(keyword == "") null else inquiry.question.contains(keyword)
}