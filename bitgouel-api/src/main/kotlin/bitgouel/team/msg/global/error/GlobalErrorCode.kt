package bitgouel.team.msg.global.error

enum class GlobalErrorCode(
    val message: String,
    val status: Int
) {
    INTERNAL_SERVER_ERROR("서버 내부 에러", 500),
    BAD_REQUEST("잘못된 요청입니다.", 400),
    INVALID_ROLE("검증되지 않은 권한입니다.", 401)
}