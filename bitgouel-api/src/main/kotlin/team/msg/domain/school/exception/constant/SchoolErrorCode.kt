package team.msg.domain.school.exception.constant

enum class SchoolErrorCode(
    val status: Int
) {
    SCHOOL_NOT_FOUND(403),
    SCHOOL_ALREADY_EXIST(409)
}