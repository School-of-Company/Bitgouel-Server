package team.msg.domain.auth.exception.constant

enum class AuthErrorCode(
    val status: Int
) {
    ALREADY_EXIST_EMAIL(409),
    ALREADY_EXIST_PHONE_NUMBER(409),
    UNAPPROVED_USER(403),
    INVALID_TOKEN(401),
    REFRESH_TOKEN_NOT_FOUND(404),
    SAME_AS_OLD_PASSWORD(409)
}