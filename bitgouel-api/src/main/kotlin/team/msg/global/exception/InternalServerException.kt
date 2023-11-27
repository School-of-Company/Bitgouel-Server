package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class InternalServerException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INTERNAL_SERVER_ERROR.status)