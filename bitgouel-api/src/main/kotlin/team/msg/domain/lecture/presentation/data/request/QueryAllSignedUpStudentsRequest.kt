package team.msg.domain.lecture.presentation.data.request

import org.springframework.web.bind.annotation.RequestParam

data class QueryAllSignedUpStudentsRequest(
    val isComplete: Boolean?
)
