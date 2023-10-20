package team.msg.domain.lecture.presentation.data.request

import team.msg.domain.lecture.enum.LectureType
import java.time.LocalDateTime

data class CreateLectureRequest (
    val name: String,
    val content: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val credit: Int,
    val maxRegisteredUser: Int
)