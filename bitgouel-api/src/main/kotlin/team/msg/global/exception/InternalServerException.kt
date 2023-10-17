package team.msg.global.exception

import team.msg.global.error.exception.BitgouelException
import team.msg.global.error.GlobalErrorCode

class InternalServerException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INTERNAL_SERVER_ERROR.status)