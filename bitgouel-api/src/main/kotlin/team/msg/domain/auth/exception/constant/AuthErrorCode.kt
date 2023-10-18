package team.msg.domain.auth.exception.constant

enum class AuthErrorCode(
    val message: String,
    val status: Int
) {
    ALREADY_SIGN_UP("이미 가입된 정보입니다.", 403)
}