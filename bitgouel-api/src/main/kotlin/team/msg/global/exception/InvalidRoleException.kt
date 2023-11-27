package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidRoleException(
    message: String
) : BitgouelException(message, GlobalErrorCode.INVALID_ROLE.status)