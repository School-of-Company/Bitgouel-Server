package team.msg.domain.eamil.exception.constant

enum class EmailErrorCode(
    val status: Int
) {
    MIS_MATCH_CODE(400),
    AUTH_CODE_EXPIRED(401),
    ALREADY_AUTHENTICATED_EMAIL(409),
    TOO_MANY_EMAIL_AUTHENTICATION_REQUEST(429),
    EMAIL_SEND_FAIL(500)
}