package team.msg.domain.student.presentation.data.response

import org.springframework.data.domain.Page
import team.msg.common.enum.ApproveStatus
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.user.model.User
import java.util.*

data class StudentActivityResponse(
    val activityId: UUID,
    val title: String,
    val userId: UUID,
    val username: String,
    val approveStatus: ApproveStatus
) {
    companion object {
        fun of(studentActivity: StudentActivity, user: User) =
            StudentActivityResponse(
                activityId = studentActivity.id,
                title = studentActivity.title,
                userId = user.id,
                username = user.name,
                approveStatus = user.approveStatus
            )
    }
}

data class StudentActivityListResponse(
    val activities: Page<StudentActivityResponse>
)