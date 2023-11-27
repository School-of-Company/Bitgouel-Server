package team.msg.domain.student.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.student.model.QStudentActivity.studentActivity
import team.msg.domain.student.repository.custom.CustomStudentActivityRepository
import java.util.*

class CustomStudentActivityRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentActivityRepository {
    override fun deleteAllByStudent(studentId: UUID) {
        queryFactory.delete(studentActivity)
            .where(studentActivity.student.id.eq(studentId))
            .execute()
    }
}