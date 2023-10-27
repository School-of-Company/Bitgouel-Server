package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class UnApprovedLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.UNAPPROVED_LECTURE.status)