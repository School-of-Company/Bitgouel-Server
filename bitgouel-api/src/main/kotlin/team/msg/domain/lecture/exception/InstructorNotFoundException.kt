package team.msg.domain.lecture.exception

import team.msg.domain.lecture.exception.constant.LectureErrorCode
import team.msg.global.error.exception.BitgouelException

class InstructorNotFoundException(
    message: String
) : BitgouelException(message, LectureErrorCode.INSTRUCTOR_NOT_FOUND.status)