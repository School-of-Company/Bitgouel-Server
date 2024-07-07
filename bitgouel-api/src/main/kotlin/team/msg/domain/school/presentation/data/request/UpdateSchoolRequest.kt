package team.msg.domain.school.presentation.data.request

import team.msg.common.enums.Line

data class UpdateSchoolRequest(
    val schoolName: String,
    val line: Line,
    val departments: List<String>
)