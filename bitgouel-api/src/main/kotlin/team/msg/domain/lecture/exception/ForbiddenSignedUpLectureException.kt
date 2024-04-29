package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenSignedUpLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.FORBIDDEN_SIGNED_UP_LECTURE.status)