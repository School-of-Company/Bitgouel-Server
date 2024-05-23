package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class EmailNotValidException(
    message: String
) : BitgouelException(message, UserErrorCode.EMAIL_NOT_VALID.status)