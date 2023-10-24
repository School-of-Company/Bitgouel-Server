package team.msg.domain.government.exception.constant

enum class GovernmentErrorCode(
    val message: String,
    val status: Int
) {
    GOVERNMENT_NOT_FOUND("존재하지 않는 유관 기관 입니다.", 404)
}