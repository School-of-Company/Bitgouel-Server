package team.msg.domain.government.exception

import team.msg.domain.government.exception.constant.GovernmentErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyExistGovernmentException(
    message: String
) : BitgouelException(message, GovernmentErrorCode.ALREADY_EXIST_GOVERNMENT.status)