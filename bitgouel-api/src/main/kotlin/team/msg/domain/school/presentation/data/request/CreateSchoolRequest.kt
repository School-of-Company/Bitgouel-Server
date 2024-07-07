package team.msg.domain.school.presentation.data.request

import team.msg.common.enums.Line
import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest

data class CreateSchoolRequest(
    val schoolName: String,
    val line: Line,
    val departments: List<String>,
    val club: List<CreateClubRequest>
)