package team.msg.domain.club.presentation.data.response

import team.msg.domain.club.model.Club

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
    }
}

data class AllClubResponse(
    val club: List<ClubResponse>
)
