package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club
import team.msg.domain.student.model.Student
import team.msg.domain.student.presentation.data.response.StudentResponse
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.presentation.data.response.TeacherResponse

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

        fun detailOf(club: Club, students: List<Student>, teacher: Teacher?) = ClubDetailsResponse(
            clubName = club.name,
            highSchoolName = club.school.highSchool.schoolName,
            headCount = students.size,
            students = students.map {
                StudentResponse(
                    id = it.id,
                    name = it.user!!.name,
                    authority = it.user!!.authority
                )
            },
            teacher = TeacherResponse(
                id = teacher?.id,
                name = teacher?.user?.name
            )
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
    val clubs: List<ClubResponse>
)

data class ClubDetailsResponse(
    val clubName: String,
    val highSchoolName: String,
    val headCount: Int,
    val students: List<StudentResponse>,
    val teacher: TeacherResponse
)

data class SchoolToClubResponse(
    val id: Long,
    val name: String
)