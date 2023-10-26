package team.msg.domain.professor.exception

import team.msg.domain.professor.exception.constant.ProfessorErrorCode
import team.msg.global.error.exception.BitgouelException

class ProfessorNotFoundException(
    message: String
) : BitgouelException(message, ProfessorErrorCode.PROFESSOR_NOT_FOUND.status)