package team.msg.domain.lecture.presentation.data.request

import team.msg.domain.lecture.enums.Division
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.enums.Semester
import java.time.LocalDateTime
import java.util.UUID

data class CreateLectureRequest (
    val name: String,
    val content: String,
    val semester: Semester,
    val division: Division,
    val department: String,
    val line: String,
    val userId: UUID,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val credit: Int,
    val maxRegisteredUser: Int
)