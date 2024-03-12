package team.msg.domain.lecture.presentation.data.response
import org.springframework.data.domain.Page
import team.msg.domain.lecture.enums.Division
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.enums.Semester
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

data class LectureResponse(
    val id: UUID,
    val name: String,
    val content: String,
    val semester: Semester,
    val division: Division,
    val department: String,
    val line: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val lectureStatus: LectureStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val lecturer: String
) {
    companion object {
        fun of(lecture: Lecture, headCount: Int): LectureResponse = LectureResponse(
            id = lecture.id,
            name = lecture.name,
            content = lecture.content,
            semester = lecture.semester,
            division = lecture.division,
            department = lecture.department,
            line = lecture.line,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            completeDate = lecture.completeDate,
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            lecturer = lecture.instructor
        )

        fun detailOf(lecture: Lecture, headCount: Int, isRegistered: Boolean): LectureDetailsResponse = LectureDetailsResponse(
            name = lecture.name,
            content = lecture.content,
            semester = lecture.semester,
            division = lecture.division,
            department = lecture.department,
            line = lecture.line,
            createAt = lecture.createdAt,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            completeDate = lecture.completeDate,
            lectureType = lecture.lectureType,
            lectureStatus = lecture.getLectureStatus(),
            headCount = headCount,
            maxRegisteredUser = lecture.maxRegisteredUser,
            isRegistered = isRegistered,
            lecturer = lecture.instructor,
            credit = lecture.credit
        )

        fun instructorOf(user: User, organization: String): InstructorResponse = InstructorResponse(
            id = user.id,
            name = user.name,
            organization = organization,
            authority = user.authority
        )

        fun lineOf(lines: List<String>): LinesResponse = LinesResponse(
            lines = lines
        )

        fun departmentOf(departments: List<String>): DepartmentsResponse = DepartmentsResponse(
            departments = departments
        )
    }
}

data class LecturesResponse(
    val lectures: Page<LectureResponse>
)

data class LectureDetailsResponse(
    val name: String,
    val content: String,
    val semester: Semester,
    val division: Division,
    val department: String,
    val line: String,
    val createAt: LocalDateTime,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val completeDate: LocalDateTime,
    val lectureType: LectureType,
    val lectureStatus: LectureStatus,
    val headCount: Int,
    val maxRegisteredUser: Int,
    val isRegistered: Boolean,
    val lecturer: String,
    val credit: Int
)

data class InstructorsResponse(
    val instructors: List<InstructorResponse>
)

data class InstructorResponse(
    val id: UUID,
    val name: String,
    val organization: String,
    val authority: Authority
)

data class LinesResponse(
    val lines: List<String>
)

data class DepartmentsResponse(
    val departments: List<String>
)