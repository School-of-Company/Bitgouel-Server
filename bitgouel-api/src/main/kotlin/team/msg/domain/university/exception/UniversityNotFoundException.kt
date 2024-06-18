package team.msg.domain.university.exception

import team.msg.domain.university.exception.constant.UniversityErrorCode
import team.msg.global.error.exception.BitgouelException

class UniversityNotFoundException(
    message: String
) : BitgouelException(message, UniversityErrorCode.UNIVERSITY_NOT_FOUND.status)