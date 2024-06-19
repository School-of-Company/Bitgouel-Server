package team.msg.domain.government.exception

import team.msg.domain.government.exception.constant.GovernmentErrorCode
import team.msg.global.error.exception.BitgouelException

class GovernmentInstructorNotFoundException(
    message: String
) : BitgouelException(message, GovernmentErrorCode.GOVERNMENT_INSTRUCTOR_NOT_FOUND.status)