package bitgouel.team.msg.domain.user.exception.constant

enum class UserErrorCode(
    val message: String,
    val status: Int
) {
    USER_NOT_FOUND("존재하지 않는 유저입니다.", 404)
}