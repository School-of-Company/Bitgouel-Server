package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club
import team.msg.domain.student.model.Student
import team.msg.domain.user.enums.Authority
import java.util.*

data class ClubResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun listOf(clubs: List<Club>): List<ClubResponse> = clubs.map {
            ClubResponse(
                id = it.id,
                name = it.name
            )
        }

        fun detailOf(club: Club, headCount: Int) = ClubDetailsResponse(
            clubName = club.name,
            highSchoolName = club.school.highSchool.schoolName,
            headCount = headCount
        )

        fun listOfStudent(students: List<Student>) = students.map {
            StudentResponse(
                id = it.user!!.id,
                name = it.user!!.name,
                authority = it.user!!.authority
            )
        }
    }
}

data class AllClubResponse(
    val club: List<ClubResponse>
)

data class ClubDetailsResponse(
    val clubName: String,
    val highSchoolName: String,
    val headCount: Int
)

data class StudentResponse(
    val id: UUID,
    val name: String,
    val authority: Authority
)

data class AllStudentsResponse(
    val students: List<StudentResponse>
)