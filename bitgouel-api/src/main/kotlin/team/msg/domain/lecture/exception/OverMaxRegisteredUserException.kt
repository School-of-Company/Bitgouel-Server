package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class OverMaxRegisteredUserException(
    message: String
) : BitgouelException(message, LectureErrorCode.OVER_MAX_REGISTERED_USER.status)