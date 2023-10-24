package team.msg.domain.bbozzak.exception.constant

enum class BbozzakErrorCode(
    val message: String,
    val status: Int
) {
    BBOZZAK_NOT_FOUND("존재하지 않는 뽀짝샘입니다.", 404)
}