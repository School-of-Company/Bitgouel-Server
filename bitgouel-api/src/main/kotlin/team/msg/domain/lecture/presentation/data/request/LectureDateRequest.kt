package team.msg.domain.lecture.presentation.data.request

import java.time.LocalDate
import java.time.LocalTime

data class LectureDateRequest(
    val completeDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
