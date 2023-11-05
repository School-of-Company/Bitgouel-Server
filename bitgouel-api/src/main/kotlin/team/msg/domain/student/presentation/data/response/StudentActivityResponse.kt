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
        fun of(studentActivities: Page<StudentActivity>, user: User): Page<StudentActivityResponse> =
            studentActivities.map {
                StudentActivityResponse(
                    activityId = it.id,
                    title = it.title,
                    userId = user.id,
                    username = user.name,
                    approveStatus = it.approveStatus
                )
            }

    }
}

data class AllStudentActivitiesResponse(
    val activities: Page<StudentActivityResponse>
)

data class StudentActivitiesByStudentResponse(
    val activities: Page<StudentActivityResponse>
)

data class MyStudentActivitiesByStudentResponse(
    val activities: Page<StudentActivityResponse>
)