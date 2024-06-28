package team.msg.domain.school.exception.constant

enum class SchoolErrorCode(
    val status: Int
) {
    INVALID_EXTENSION(400),
    SCHOOL_NOT_FOUND(403),
    SCHOOL_ALREADY_EXIST(409)
}