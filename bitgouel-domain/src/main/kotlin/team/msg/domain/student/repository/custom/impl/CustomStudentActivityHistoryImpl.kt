package team.msg.domain.student.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.student.model.QStudentActivityHistory
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.custom.CustomStudentActivityHistoryRepository

class CustomStudentActivityHistoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentActivityHistoryRepository {
    override fun deleteAllByStudent(student: Student) {
        val studentActivityHistory = QStudentActivityHistory.studentActivityHistory
        queryFactory.delete(studentActivityHistory)
            .where(studentActivityHistory.student.eq(student))
            .execute()
    }
}