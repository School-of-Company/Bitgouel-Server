package team.msg.domain.lecture.presentation.data.web

import org.springframework.web.bind.annotation.RequestParam

data class QueryAllLinesWebRequest (
    @RequestParam
    val division: String,

    @RequestParam(required = false)
    val keyword: String = ""
)