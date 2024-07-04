package team.msg.domain.lecture.presentation.data.request

import team.msg.domain.lecture.enums.Semester
import java.time.LocalDateTime
import java.util.UUID

data class UpdateLectureRequest (
    val name: String,
    val content: String,
    val semester: Semester,
    val division: String,
    val department: String,
    val line: String,
    val userId: UUID,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val lectureDates: List<LectureDateRequest>,
    val lectureType: String,
    val credit: Int,
    val maxRegisteredUser: Int,
    val address: String,
    val locationDetails: String,
    val essentialComplete: Boolean
)