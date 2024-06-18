package team.msg.domain.university.exception

import team.msg.domain.university.exception.constant.ProfessorErrorCode
import team.msg.global.error.exception.BitgouelException

class ProfessorNotFoundException(
    message: String
) : BitgouelException(message, ProfessorErrorCode.PROFESSOR_NOT_FOUND.status)