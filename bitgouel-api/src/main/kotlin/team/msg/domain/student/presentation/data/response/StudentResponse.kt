package team.msg.domain.student.presentation.data.response

import team.msg.domain.student.model.Student
import java.util.*

class StudentResponse(
    val id: UUID,
    val userId: UUID,
    val name: String
) {
    companion object {
        fun listOf(students: List<Student>) = students.map {
            StudentResponse(
                id = it.id,
                userId = it.user!!.id,
                name = it.user!!.name
            )
        }

        fun detailOf(student: Student) = StudentDetailsResponse(
            name = student.user!!.name,
            phoneNumber = student.user!!.phoneNumber,
            email = student.user!!.email,
            credit = student.credit
        )
    }
}

data class StudentDetailsResponse(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val credit: Int
)