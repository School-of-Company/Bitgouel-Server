package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class ExpiredTokenException(
    message: String
) : BitgouelException(message, GlobalErrorCode.EXPIRED_TOKEN.status)