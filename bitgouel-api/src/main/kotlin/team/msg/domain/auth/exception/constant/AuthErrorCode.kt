package team.msg.domain.auth.exception.constant

enum class AuthErrorCode(
    val message: String,
    val status: Int
) {
    ALREADY_EXIST_EMAIL("이미 가입된 이메일입니다.", 409),
    ALREADY_EXIST_PHONE_NUMBER("이미 가입된 전화번호입니다.", 409),
    MISMATCH_PASSWORD("일치하지 않는 비밀번호입니다.", 401),
    UNAPPROVED_USER("아직 회원가입 대기 상태입니다.", 401)
}