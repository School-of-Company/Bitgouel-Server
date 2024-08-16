package team.msg.domain.lecture.presentation.data.web

import org.springframework.web.bind.annotation.RequestParam

data class QueryAllSignedUpStudentsWebRequest(
    @RequestParam(required = false, name = "is_complete")
    val isComplete: Boolean?
)
