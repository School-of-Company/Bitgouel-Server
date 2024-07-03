package team.msg.domain.university.exception

import team.msg.domain.university.exception.constant.UniversityErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyExistUniversityNameException(
    message: String
) : BitgouelException(message, UniversityErrorCode.ALREADY_EXIST_UNIVERSITY_NAME_EXCEPTION.status)