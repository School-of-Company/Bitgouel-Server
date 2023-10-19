package team.msg.domain.lecture.exception.constant

enum class LectureErrorCode(
    val message: String,
    val status: Int
){
    INVALID_LECTURE_TYPE("유효하지 않은 강의 구분입니다.", 400)
}