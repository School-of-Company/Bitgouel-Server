package team.msg.common.util

import org.springframework.stereotype.Component
import team.msg.common.ulid.ULIDGenerator
import team.msg.domain.club.model.Club
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.model.User
import java.util.*

@Component
class StudentUtil(
    private val studentRepository: StudentRepository
) {

    fun createStudent(user: User, club: Club, grade: Int, classRoom: Int, number: Int, admissionNumber: Int, subscriptionGrade: Int) {
        val student = Student(
            id = UUID(0, 0),
            ulid = ULIDGenerator.generateULID(),
            user = user,
            club = club,
            grade = grade,
            classRoom = classRoom,
            number = number,
            cohort = admissionNumber - 2020,
            credit = 0,
            subscriptionGrade = subscriptionGrade,
            studentRole = StudentRole.STUDENT
        )
        studentRepository.save(student)
    }
}