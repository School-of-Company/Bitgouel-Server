package team.msg.domain.lecture.presentation.data.request

import team.msg.domain.lecture.enums.Division

data class QueryAllLinesRequest (
    val division: Division,
    val keyword: String?
)