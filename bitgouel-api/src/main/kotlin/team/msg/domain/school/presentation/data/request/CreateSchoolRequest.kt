package team.msg.domain.school.presentation.data.request

import team.msg.common.enums.Field
import team.msg.common.enums.Line

data class CreateSchoolRequest(
    val schoolName: String,
    val field: Field,
    val line: Line
)