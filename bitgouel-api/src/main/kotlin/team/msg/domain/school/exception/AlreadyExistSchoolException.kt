package team.msg.domain.school.exception

import team.msg.domain.school.exception.constant.SchoolErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyExistSchoolException(
    message: String
) : BitgouelException(message, SchoolErrorCode.SCHOOL_ALREADY_EXIST.status)