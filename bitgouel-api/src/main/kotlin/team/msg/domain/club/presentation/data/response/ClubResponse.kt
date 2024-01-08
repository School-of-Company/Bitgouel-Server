package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club

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

        fun detailOf(club: Club, headCount: Int) = ClubDetailsResponse(
            clubName = club.name,
            highSchoolName = club.school.highSchool.schoolName,
            headCount = headCount
        )
    }
}

data class ClubsResponse(
    val club: List<ClubResponse>
)

data class ClubDetailsResponse(
    val clubName: String,
    val highSchoolName: String,
    val headCount: Int
)