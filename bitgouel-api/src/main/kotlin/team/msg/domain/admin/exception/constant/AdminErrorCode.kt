package team.msg.domain.admin.exception.constant

enum class AdminErrorCode(
    val status: Int
) {
    INVALID_CELL_TYPE(400),
    ADMIN_NOT_FOUND(404),
    ALREADY_EXIST_STUDENT_INFORMATION(409)
}