package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.lecture.model.QRegisteredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.student.model.Student

class CustomRegisteredLectureRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomRegisteredLectureRepository {
    override fun deleteAllByStudent(student: Student) {
        val registeredLecture = QRegisteredLecture.registeredLecture
        queryFactory.delete(registeredLecture)
            .where(registeredLecture.student.id.eq(student.id))
            .execute()
    }
}