package team.msg.domain.student.exception

import team.msg.domain.student.exception.constant.StudentErrorCode
import team.msg.global.error.exception.BitgouelException

class StudentNotFoundException(
    message: String
) : BitgouelException(message, StudentErrorCode.STUDENT_NOT_FOUND.status)