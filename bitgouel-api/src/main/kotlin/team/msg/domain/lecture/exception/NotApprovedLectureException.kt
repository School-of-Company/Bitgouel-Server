package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class NotApprovedLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.NOT_APPROVED_LECTURE.status)