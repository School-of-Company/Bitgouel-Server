package team.msg.domain.teacher.exception.constant

enum class TeacherErrorCode(
    val message: String,
    val status: Int
) {
    TEACHER_NOT_FOUND("취업 동아리 선생님을 찾을 수 없습니다.", 404)
}