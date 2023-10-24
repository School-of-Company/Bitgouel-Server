package team.msg.domain.teacher.exception

import team.msg.domain.teacher.exception.constant.TeacherErrorCode
import team.msg.global.error.exception.BitgouelException

class TeacherNotFoundException(
    message: String
) : BitgouelException(message, TeacherErrorCode.TEACHER_NOT_FOUND.status)