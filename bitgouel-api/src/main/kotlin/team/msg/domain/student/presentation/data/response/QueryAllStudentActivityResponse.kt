package team.msg.domain.student.presentation.data.response

import team.msg.common.enum.ApproveStatus
import java.util.UUID

data class QueryAllStudentActivityResponse(
    val activityId: UUID,
    val title: String,
    val userId: UUID,
    val userName: String,
    val approveStatus: ApproveStatus
)