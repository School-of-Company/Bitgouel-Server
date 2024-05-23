package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class PasswordNotValidException(
    message: String
) : BitgouelException(message, UserErrorCode.PASSWORD_NOT_VALID.status)