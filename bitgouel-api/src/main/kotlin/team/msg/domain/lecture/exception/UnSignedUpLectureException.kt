package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class UnSignedUpLectureException(
    message: String
) : BitgouelException(message, LectureErrorCode.UNSIGNED_UP_LECTURE.status)