package team.msg.domain.eamil.exception.constant

enum class EmailErrorCode(
    val status: Int
) {
    ALREADY_AUTHENTICATED_EMAIL(409),
    TOO_MANY_EMAIL_AUTHENTICATION_REQUEST(429),
    EMAIL_SEND_FAIL(500)
}