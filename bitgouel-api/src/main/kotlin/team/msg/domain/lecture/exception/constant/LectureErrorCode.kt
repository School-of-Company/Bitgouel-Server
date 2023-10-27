package team.msg.domain.lecture.exception.constant

enum class LectureErrorCode(
    val status: Int
){
    INVALID_LECTURE_TYPE(400),

    LECTURE_NOT_FOUND(404),

    ALREADY_APPROVED_LECTURE(409),
    ALREADY_SIGNED_UP_LECTURE(409),
    NOT_APPROVED_LECTURE(409),
    OVER_MAX_REGISTERED_USER(409),
    MISS_SIGN_UP_ABLE_DATE(409)
}