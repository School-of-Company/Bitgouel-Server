package team.msg.domain.school.exception

import team.msg.domain.school.exception.constant.SchoolErrorCode
import team.msg.global.error.exception.BitgouelException

class SchoolNotFoundException(
    message: String
) : BitgouelException(message, SchoolErrorCode.SCHOOL_NOT_FOUND.status)