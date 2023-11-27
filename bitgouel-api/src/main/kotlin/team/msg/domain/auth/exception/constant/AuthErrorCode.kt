package team.msg.domain.auth.exception.constant

enum class AuthErrorCode(
    val status: Int
) {
    ALREADY_EXIST_EMAIL(409),
    ALREADY_EXIST_PHONE_NUMBER(409),
    MISMATCH_PASSWORD(401),
    UNAPPROVED_USER(403),
    INVALID_TOKEN(401),
    REFRESH_TOKEN_NOT_FOUND(404)
}