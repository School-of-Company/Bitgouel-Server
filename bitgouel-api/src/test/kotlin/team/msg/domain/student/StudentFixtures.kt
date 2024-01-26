package team.msg.domain.student

import team.msg.domain.club.model.Club
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student
import team.msg.domain.user.model.User
import java.util.UUID

fun createStudent(
    studentId: UUID,
    user: User,
    club: Club,
    grade: Int,
    classRoom: Int,
    number: Int,
    cohort: Int,
    credit: Int,
    studentRole: StudentRole
) = Student(
    id = studentId,
    user = user,
    club = club,
    grade = grade,
    classRoom = classRoom,
    number = number,
    cohort = cohort,
    credit = credit,
    studentRole = studentRole
)