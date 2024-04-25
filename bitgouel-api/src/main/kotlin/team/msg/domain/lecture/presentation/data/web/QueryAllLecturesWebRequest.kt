package team.msg.domain.lecture.presentation.data.web

import org.springframework.web.bind.annotation.RequestParam

data class QueryAllLecturesWebRequest(
    @RequestParam
    val type: String?
)
