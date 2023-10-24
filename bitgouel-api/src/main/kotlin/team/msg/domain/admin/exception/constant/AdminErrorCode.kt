package team.msg.domain.admin.exception.constant

enum class AdminErrorCode(
    val message: String,
    val status: Int
) {
    ADMIN_NOT_FOUND("존재하지 않는 어드민 입니다.", 404)
}