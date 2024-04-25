package team.msg.domain.lecture.presentation.data.request

data class QueryAllLinesRequest (
    val division: String,
    val keyword: String?
)