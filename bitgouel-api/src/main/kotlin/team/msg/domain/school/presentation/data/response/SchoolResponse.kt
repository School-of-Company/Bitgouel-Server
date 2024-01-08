package team.msg.domain.school.presentation.data.response

import team.msg.domain.club.presentation.data.response.SchoolToClubResponse

data class SchoolResponse(
    val id: Long,
    val schoolName: String,
    val clubs: List<SchoolToClubResponse>
)

data class SchoolsResponse(
    val schools: List<SchoolResponse>
)