package team.msg.domain.school.exception.constant

enum class SchoolErrorCode(
    val message: String,
    val status: Int
) {
    SCHOOL_NOT_FOUND("이미 가입된 정보입니다.", 403)
}