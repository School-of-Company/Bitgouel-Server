package team.msg.domain.school.presentation.data.response

import team.msg.common.enums.Line
import team.msg.domain.club.presentation.data.response.SchoolToClubResponse

data class SchoolResponse(
    val id: Long,
    val name: String,
    val line: Line,
    val departments: List<String>,
    val logoImageUrl: String,
    val clubs: List<SchoolToClubResponse>
)

data class SchoolsResponse(
    val schools: List<SchoolResponse>
)

data class SchoolNameResponse(
    val name: String
)

data class SchoolNamesResponse(
    val schools: List<SchoolNameResponse>
)