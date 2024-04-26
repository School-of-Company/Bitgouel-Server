package team.msg.domain.lecture.presentation.data.web

import org.springframework.web.bind.annotation.RequestParam

data class QueryAllDivisionsWebRequest(
    @RequestParam(required = false)
    val keyword: String? = null
)