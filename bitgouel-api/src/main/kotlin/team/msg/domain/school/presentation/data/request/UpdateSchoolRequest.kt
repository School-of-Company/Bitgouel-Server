package team.msg.domain.school.presentation.data.request

import team.msg.common.enums.Field
import team.msg.common.enums.Line
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest

data class UpdateSchoolRequest(
    val schoolName: String,
    val field: Field,
    val line: Line,
    val departments: List<String>,
    val club: List<UpdateClubRequest>
)