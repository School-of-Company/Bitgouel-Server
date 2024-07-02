package team.msg.domain.government.exception

import team.msg.domain.government.exception.constant.GovernmentErrorCode
import team.msg.global.error.exception.BitgouelException

class GovernmentNotFoundException(
    message: String
) : BitgouelException(message, GovernmentErrorCode.GOVERNMENT_NOT_FOUND.status)