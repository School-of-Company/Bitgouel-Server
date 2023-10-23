package team.msg.domain.lecture.exception.constant

enum class LectureErrorCode(
    val message: String,
    val status: Int
){
    INVALID_LECTURE_TYPE("유효하지 않은 강의 구분입니다.", 400),

    LECTURE_NOT_FOUND("존재하지 않는 강의입니다.", 404),

    ALREADY_APPROVED_LECTURE("이미 개설 신청이 승인된 강의입니다.",409)
}