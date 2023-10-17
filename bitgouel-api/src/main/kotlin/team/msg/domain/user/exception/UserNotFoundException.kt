package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class UserNotFoundException(
    message: String
) : BitgouelException(message, UserErrorCode.USER_NOT_FOUND.status)