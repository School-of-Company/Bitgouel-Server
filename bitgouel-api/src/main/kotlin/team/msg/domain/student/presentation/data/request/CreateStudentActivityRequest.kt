package team.msg.domain.student.presentation.data.request

import java.time.LocalDate

data class CreateStudentActivityRequest(
    val title: String,
    val content: String,
    val credit: Int,
    val activityDate: LocalDate
)