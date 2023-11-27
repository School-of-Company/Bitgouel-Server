package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidLectureTypeException(
    message: String
) : BitgouelException(message, LectureErrorCode.INVALID_LECTURE_TYPE.status)