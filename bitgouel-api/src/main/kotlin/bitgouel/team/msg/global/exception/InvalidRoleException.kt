package bitgouel.team.msg.global.exception

import bitgouel.team.msg.global.error.GlobalErrorCode
import bitgouel.team.msg.global.error.exception.BitgouelException

class InvalidRoleException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INVALID_ROLE.status)