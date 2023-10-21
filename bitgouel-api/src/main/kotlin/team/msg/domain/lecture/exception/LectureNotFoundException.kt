package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class LectureNotFoundException(
    message: String
) : BitgouelException(message, LectureErrorCode.LECTURE_NOT_FOUND.status)