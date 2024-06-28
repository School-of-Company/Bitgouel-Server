package team.msg.domain.university.exception

import team.msg.domain.university.exception.constant.UniversityErrorCode
import team.msg.global.error.exception.BitgouelException
class ProfessorNotFoundException(
    message: String
) : BitgouelException(message, UniversityErrorCode.PROFESSOR_NOT_FOUND.status)