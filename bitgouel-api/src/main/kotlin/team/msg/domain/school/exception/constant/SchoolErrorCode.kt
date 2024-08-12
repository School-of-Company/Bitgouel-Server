package team.msg.domain.school.exception.constant

enum class SchoolErrorCode(
    val status: Int
) {
    INVALID_EXTENSION(400),
    NOT_EMPTY_SCHOOL(400),
    SCHOOL_NOT_FOUND(404),
    SCHOOL_ALREADY_EXIST(409)
}