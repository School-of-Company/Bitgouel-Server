package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.exception.BitgouelException
import bitgouel.team.msg.global.error.GlobalErrorCode

class InternalServerException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INTERNAL_SERVER_ERROR.status)