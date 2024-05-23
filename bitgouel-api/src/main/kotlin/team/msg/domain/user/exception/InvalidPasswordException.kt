package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidPasswordException(
    message: String
) : BitgouelException(message, UserErrorCode.PASSWORD_NOT_VALID.status)