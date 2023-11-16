package team.msg.domain.lecture.presentation.data.response
import org.springframework.data.domain.Page
import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.model.Lecture
import java.time.LocalDateTime
import java.util.*

data class LectureResponse(
    val id: UUID,
    val name: String,
    val content: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val lectureStatus: LectureStatus,
    val approveStatus: ApproveStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val lecturer: String
) {
    companion object {
        fun of(lecture: Lecture, headCount: Int): LectureResponse = LectureResponse(
            id = lecture.id,
            name = lecture.name,
            content = lecture.content,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            completeDate = lecture.completeDate,
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            approveStatus = lecture.approveStatus,
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            lecturer = lecture.instructor
        )

        fun detailOf(lecture: Lecture, headCount: Int): LectureDetailsResponse = LectureDetailsResponse(
            name = lecture.name,
            content = lecture.content,
            createAt = lecture.createdAt,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            completeDate = lecture.completeDate,
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            lecturer = lecture.instructor,
            credit = lecture.credit
        )
    }
}

data class LecturesResponse(
    val lectures: Page<LectureResponse>
)

data class LectureDetailsResponse(
    val name: String,
    val content: String,
    val createAt: LocalDateTime,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val lectureStatus: LectureStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val lecturer: String,
    val credit: Int
)