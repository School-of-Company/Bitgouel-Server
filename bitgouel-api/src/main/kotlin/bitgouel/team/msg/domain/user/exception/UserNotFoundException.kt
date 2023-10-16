package bitgouel.team.msg.domain.user.exception

import bitgouel.team.msg.domain.user.exception.constant.UserErrorCode
import bitgouel.team.msg.global.error.exception.BitgouelException

class UserNotFoundException(
    message: String
) : BitgouelException(message, UserErrorCode.USER_NOT_FOUND.status)