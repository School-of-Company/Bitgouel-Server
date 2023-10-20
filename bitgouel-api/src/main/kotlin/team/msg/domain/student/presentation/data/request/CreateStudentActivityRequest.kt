package team.msg.domain.student.presentation.data.request

import java.time.LocalDateTime

data class CreateStudentActivityRequest(
    val title: String,
    val content: String,
    val credit: Int,
    val activityDate: LocalDateTime
)