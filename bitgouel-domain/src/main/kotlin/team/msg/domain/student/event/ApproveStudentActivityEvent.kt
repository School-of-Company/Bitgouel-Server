package team.msg.domain.student.event

import team.msg.domain.student.model.StudentActivity

/**
 * StudentActivity를 승인하는 이벤트를 나타냅니다.
 * StudentActivity를 구독자에게 알리기 위해서 사용됩니다.
 */
data class ApproveStudentActivityEvent(
    val studentActivity: StudentActivity
)