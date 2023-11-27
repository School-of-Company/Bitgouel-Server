package team.msg.domain.student.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.student.model.QStudentActivityHistory
import team.msg.domain.student.model.Student

class CustomStudentActivityHistoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentActivityHistoryRepository {
    override fun deleteAllByStudent(student: Student) {
        val studentActivityHistory = QStudentActivityHistory.studentActivityHistory
        queryFactory.delete(studentActivityHistory)
            .where(studentActivityHistory.student.eq(student))
    }
}