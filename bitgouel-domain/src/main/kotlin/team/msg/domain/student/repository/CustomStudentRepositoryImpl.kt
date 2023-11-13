package team.msg.domain.student.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.certifiacation.model.QCertification.certification
import java.util.*

class CustomStudentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentRepository {
    override fun existOne(id: UUID): Boolean {
        val fetchOne = queryFactory.selectOne()
            .from(certification)
            .where(certification.id.eq(id))
            .fetchFirst()

        return fetchOne != null
    }
}