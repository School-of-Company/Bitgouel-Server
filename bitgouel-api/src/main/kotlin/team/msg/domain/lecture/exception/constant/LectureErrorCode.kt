package team.msg.domain.lecture.exception.constant

enum class LectureErrorCode(
    val status: Int
){
    INVALID_LECTURE_TYPE(400),
    LECTURE_NOT_FOUND(404),
    ALREADY_APPROVED_LECTURE(409)
}