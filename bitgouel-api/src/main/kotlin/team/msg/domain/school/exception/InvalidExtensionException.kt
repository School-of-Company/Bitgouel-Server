package team.msg.domain.school.exception

import team.msg.domain.school.exception.constant.SchoolErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidExtensionException(
    message: String
) : BitgouelException(message, SchoolErrorCode.INVALID_EXTENSION.status)