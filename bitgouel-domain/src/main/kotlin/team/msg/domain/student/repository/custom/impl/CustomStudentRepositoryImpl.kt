package team.msg.domain.student.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import team.msg.domain.club.model.Club
import team.msg.domain.student.model.QStudent
import team.msg.domain.student.repository.custom.CustomStudentRepository

class CustomStudentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomStudentRepository {

//    override fun countByClubAndGrade(club: Club, grade: Int?): Int =
//        queryFactory.selectFrom(QStudent.student.count())

}