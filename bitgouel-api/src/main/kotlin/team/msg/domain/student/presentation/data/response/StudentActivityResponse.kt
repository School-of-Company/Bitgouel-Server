package team.msg.domain.student.presentation.data.response

import org.springframework.data.domain.Page
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class StudentActivityResponse(
    val activityId: UUID,
    val title: String,
    val activityDate: LocalDate,
    val userId: UUID,
    val username: String
) {
    companion object {
        fun pageOf(studentActivities: Page<StudentActivity>, user: User): Page<StudentActivityResponse> =
            studentActivities.map {
                StudentActivityResponse(
                    activityId = it.id,
                    title = it.title,
                    activityDate = it.activityDate,
                    userId = user.id,
                    username = user.name
                )
            }

        fun detailOf(studentActivity: StudentActivity): StudentActivityDetailsResponse =
            studentActivity.run {
                StudentActivityDetailsResponse(
                    id = id,
                    title = title,
                    content = content,
                    credit = credit,
                    activityDate = activityDate,
                    modifiedAt = modifiedAt
                )
            }
    }
}

data class StudentActivitiesResponse(
    val activities: Page<StudentActivityResponse>
)

data class StudentActivityDetailsResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val credit: Int,
    val activityDate: LocalDate,
    val modifiedAt: LocalDateTime
)