package team.msg.domain.user.exception.constant

enum class UserErrorCode(
    val status: Int
) {
    USER_NOT_FOUND(404),
    USER_ALREADY_APPROVED(409)
}