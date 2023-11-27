package team.msg.domain.certifiacation.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.certifiacation.model.QCertification.certification
import team.msg.domain.certifiacation.repository.custom.CustomCertificationRepository
import java.util.*

class CustomCertificationRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomCertificationRepository {
    override fun deleteAllByStudentId(studentId: UUID) {
        queryFactory.delete(certification)
            .where(certification.studentId.eq(studentId))
            .execute()
    }
}