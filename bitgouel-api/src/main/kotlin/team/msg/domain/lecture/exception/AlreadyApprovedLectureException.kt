package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyApprovedLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.ALREADY_APPROVED_LECTURE.status)