package team.msg.domain.government.exception.constant

enum class GovernmentErrorCode(
    val status: Int
) {
    GOVERNMENT_NOT_FOUND(404),
    GOVERNMENT_INSTRUCTOR_NOT_FOUND(404),
    ALREADY_EXIST_GOVERNMENT(409)
}