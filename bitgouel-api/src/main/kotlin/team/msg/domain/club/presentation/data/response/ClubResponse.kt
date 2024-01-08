package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club
import team.msg.domain.school.model.School
import team.msg.domain.student.model.Student
import team.msg.domain.student.presentation.data.response.StudentResponse

data class ClubResponse(
    val id: Long,
    val name: String,
    val schoolName: String
) {
    companion object {
        fun listOf(clubs: List<Club>): List<ClubResponse> = clubs.map {
            ClubResponse(
                id = it.id,
                name = it.name,
                schoolName = it.school.highSchool.schoolName
            )
        }

        fun detailOf(club: Club, headCount: Int, students: List<Student>) = ClubDetailsResponse(
            clubName = club.name,
            highSchoolName = club.school.highSchool.schoolName,
            headCount = headCount,
            students = students.map {
                StudentResponse(
                    id = it.id,
                    name = it.user!!.name,
                    authority = it.user!!.authority
                )
            }
        )

        fun schoolOf(clubs: List<Club>) = clubs.map {
            SchoolToClubResponse(
                id = it.id,
                name = it.name
            )
        }
    }
}

data class ClubsResponse(
    val club: List<ClubResponse>
)

data class ClubDetailsResponse(
    val clubName: String,
    val highSchoolName: String,
    val headCount: Int,
    val students: List<StudentResponse>
)

data class SchoolToClubResponse(
    val id: Long,
    val name: String
)