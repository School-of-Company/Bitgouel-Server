package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class AuthCodeExpiredException(
    message: String
) : BitgouelException(message, EmailErrorCode.AUTH_CODE_EXPIRED.status)