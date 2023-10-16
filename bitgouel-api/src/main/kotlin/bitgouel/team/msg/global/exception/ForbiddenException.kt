package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.GlobalErrorCode
import bitgouel.team.msg.global.error.exception.BitgouelException

class ForbiddenException(
    message: String
) : BitgouelException(message, GlobalErrorCode.FORBIDDEN.status)