package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.exception.BitgouelException
import bitgouel.team.msg.global.error.ErrorCode

class InternalServerException(
    message: String
) : BitgouelException(ErrorCode.INTERNAL_SERVER_ERROR) {
    init {
        super.errorCode.message = message
    }
}