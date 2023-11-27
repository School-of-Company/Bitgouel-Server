package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class NotAvailableSignUpDateException (
    message: String
) : BitgouelException(message, LectureErrorCode.NOT_AVAILABLE_SIGN_UP_DATE.status)