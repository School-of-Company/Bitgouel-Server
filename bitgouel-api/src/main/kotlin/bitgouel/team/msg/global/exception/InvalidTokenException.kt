package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.GlobalErrorCode
import bitgouel.team.msg.global.error.exception.BitgouelException

class InvalidTokenException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INVALID_TOKEN.status)