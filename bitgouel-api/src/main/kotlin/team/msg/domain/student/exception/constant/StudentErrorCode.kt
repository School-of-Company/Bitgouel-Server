package team.msg.domain.student.exception.constant

enum class StudentErrorCode(
    val message: String,
    val status: Int
) {
    STUDENT_NOT_FOUND("학생을 찾을 수 없습니다.", 404)
}