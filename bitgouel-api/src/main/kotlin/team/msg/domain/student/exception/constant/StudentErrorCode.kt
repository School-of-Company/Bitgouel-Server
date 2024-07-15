package team.msg.domain.student.exception.constant

enum class StudentErrorCode(
    val status: Int
) {
    STUDENT_NOT_FOUND(404),
    INVALID_STUDENT_GRADE(409)
}