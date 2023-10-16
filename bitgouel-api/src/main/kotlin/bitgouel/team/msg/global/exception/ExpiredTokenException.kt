package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.GlobalErrorCode
import bitgouel.team.msg.global.error.exception.BitgouelException

class ExpiredTokenException(
    message: String
) : BitgouelException(message, GlobalErrorCode.EXPIRED_TOKEN.status)