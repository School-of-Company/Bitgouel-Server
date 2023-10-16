package bitgouel.team.msg.global.error

enum class GlobalErrorCode(
    val message: String,
    val status: Int
) {
    INTERNAL_SERVER_ERROR("서버 내부 에러", 500),
    BAD_REQUEST("잘못된 요청입니다.", 400),
    INVALID_ROLE("검증되지 않은 권한입니다.", 401),
    FORBIDDEN("FORBIDDEN", 403),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", 401),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401)
}