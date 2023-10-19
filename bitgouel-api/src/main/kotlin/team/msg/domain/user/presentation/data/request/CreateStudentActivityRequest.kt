package team.msg.domain.user.presentation.data.request

import java.time.LocalDateTime

data class CreateStudentActivityRequest(
    val title: String,
    val content: String,
    val credit: Int,
    val createdAt: LocalDateTime
)