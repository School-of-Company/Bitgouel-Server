package team.msg.domain.student.exception

import team.msg.domain.student.exception.constant.StudentActivityErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenStudentActivityException(
    message: String
) : BitgouelException(message, StudentActivityErrorCode.FORBIDDEN_STUDENT_ACTIVITY.status) {
}