package team.msg.domain.student.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.student.model.QStudentActivity
import team.msg.domain.student.model.Student

class CustomStudentActivityRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentActivityRepository {
    override fun deleteAllByStudent(student: Student) {
        val studentActivity = QStudentActivity.studentActivity
        queryFactory.delete(studentActivity)
            .where(QStudentActivity.studentActivity.student.eq(student))
    }
}