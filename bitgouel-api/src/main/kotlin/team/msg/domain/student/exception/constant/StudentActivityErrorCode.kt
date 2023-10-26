package team.msg.domain.student.exception.constant

enum class StudentActivityErrorCode(
    val status: Int
) {
    FORBIDDEN_STUDENT_ACTIVITY(403),
    STUDENT_ACTIVITY_NOT_FOUND(404)
}