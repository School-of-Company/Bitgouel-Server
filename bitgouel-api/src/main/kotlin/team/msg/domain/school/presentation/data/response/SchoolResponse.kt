package team.msg.domain.school.presentation.data.response

import team.msg.domain.club.model.Club

data class SchoolResponse(
    val id: Long,
    val schoolName: String,
    val clubs: List<Club>
)

data class SchoolsResponse(
    val school: List<SchoolResponse>
)