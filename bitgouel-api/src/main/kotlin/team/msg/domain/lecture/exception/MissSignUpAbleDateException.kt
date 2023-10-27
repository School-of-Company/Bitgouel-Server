package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class MissSignUpAbleDateException (
    message: String
) : BitgouelException(message, LectureErrorCode.MISS_SIGN_UP_ABLE_DATE.status)