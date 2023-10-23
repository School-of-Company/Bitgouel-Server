package team.msg.domain.student.exception.constant

enum class StudentActivityErrorCode(
    val message: String,
    val status: Int
) {
    STUDENT_ACTIVITY_NOT_FOUND("학생 활동을 찾을 수 없습니다", 404)
}