package team.msg.domain.student.presentation.data.response

import org.springframework.data.domain.Page
import team.msg.common.enums.ApproveStatus
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

data class StudentActivityResponse(
    val activityId: UUID,
    val title: String,
    val userId: UUID,
    val username: String,
    val approveStatus: ApproveStatus
) {
    companion object {
        fun pageOf(studentActivities: Page<StudentActivity>, user: User): Page<StudentActivityResponse> =
            studentActivities.map {
                StudentActivityResponse(
                    activityId = it.id,
                    title = it.title,
                    userId = user.id,
                    username = user.name,
                    approveStatus = it.approveStatus
                )
            }

        fun detailOf(studentActivity: StudentActivity): StudentActivityDetailsResponse =
            StudentActivityDetailsResponse(
                id = studentActivity.id,
                title = studentActivity.title,
                content = studentActivity.content,
                credit = studentActivity.credit,
                activityDate = studentActivity.activityDate,
                modifiedAt = studentActivity.modifiedAt
            )
    }
}

data class AllStudentActivitiesResponse(
    val activities: Page<StudentActivityResponse>
)

data class StudentActivitiesResponse(
    val activities: Page<StudentActivityResponse>
)

data class StudentActivityDetailsResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val credit: Int,
    val activityDate: LocalDateTime,
    val modifiedAt: LocalDateTime
)