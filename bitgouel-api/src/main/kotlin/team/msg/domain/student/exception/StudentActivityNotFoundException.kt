package team.msg.domain.student.exception

import team.msg.domain.student.exception.constant.StudentActivityErrorCode
import team.msg.global.error.exception.BitgouelException

class StudentActivityNotFoundException(
    message: String
) : BitgouelException(message, StudentActivityErrorCode.STUDENT_ACTIVITY_NOT_FOUND.status)