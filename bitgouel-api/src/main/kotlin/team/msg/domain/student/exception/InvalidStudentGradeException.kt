package team.msg.domain.student.exception

import team.msg.domain.student.exception.constant.StudentErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidStudentGradeException(
    message: String
) : BitgouelException(message, StudentErrorCode.INVALID_STUDENT_GRADE.status)