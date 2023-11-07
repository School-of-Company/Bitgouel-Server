package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club
import team.msg.domain.school.enums.HighSchool

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

        fun detailOf(club: Club,highSchool: HighSchool,headCount: Int) = ClubDetailsResponse(
                clubName = club.name,
                highSchoolName = highSchool.schoolName,
                headCount = headCount
            )
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