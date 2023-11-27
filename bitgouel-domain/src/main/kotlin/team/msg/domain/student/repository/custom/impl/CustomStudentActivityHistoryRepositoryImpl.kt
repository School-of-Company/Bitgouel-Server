package team.msg.domain.student.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.student.model.QStudentActivityHistory.studentActivityHistory
import team.msg.domain.student.repository.custom.CustomStudentActivityHistoryRepository
import java.util.*

class CustomStudentActivityHistoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentActivityHistoryRepository {
    override fun deleteAllByStudent(studentId: UUID) {
        queryFactory.delete(studentActivityHistory)
            .where(studentActivityHistory.student.id.eq(studentId))
            .execute()
    }
}