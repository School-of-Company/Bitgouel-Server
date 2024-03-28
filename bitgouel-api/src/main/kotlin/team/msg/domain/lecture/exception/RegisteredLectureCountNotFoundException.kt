package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class RegisteredLectureCountNotFoundException(
    message: String
) : BitgouelException(message, LectureErrorCode.REGISTERED_LECTURE_COUNT_NOT_FOUND.status) {
}