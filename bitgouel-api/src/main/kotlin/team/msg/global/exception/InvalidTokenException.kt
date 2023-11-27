package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidTokenException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INVALID_TOKEN.status)