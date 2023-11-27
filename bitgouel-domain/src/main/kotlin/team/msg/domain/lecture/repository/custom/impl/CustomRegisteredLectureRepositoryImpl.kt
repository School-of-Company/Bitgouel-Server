package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.lecture.model.QRegisteredLecture.registeredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import java.util.*

class CustomRegisteredLectureRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomRegisteredLectureRepository {
    override fun deleteAllByStudentId(studentId: UUID) {
        queryFactory.delete(registeredLecture)
            .where(registeredLecture.student.id.eq(studentId))
            .execute()
    }
}