package team.msg.domain.user.exception.constant

enum class UserErrorCode(
    val status: Int
) {
    EMAIL_NOT_VALID(400),
    PASSWORD_NOT_VALID(400),
    PHONE_NUMBER_NOT_VALID(400),
    USER_NOT_FOUND(404),
    USER_ALREADY_APPROVED(409)
}