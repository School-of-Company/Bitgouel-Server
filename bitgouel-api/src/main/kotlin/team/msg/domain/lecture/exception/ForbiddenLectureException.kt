package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.FORBIDDEN_LECTURE.status)