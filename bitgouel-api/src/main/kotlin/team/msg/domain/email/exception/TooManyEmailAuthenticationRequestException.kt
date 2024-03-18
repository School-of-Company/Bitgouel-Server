package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class TooManyEmailAuthenticationRequestException(
    message: String
) : BitgouelException(message, EmailErrorCode.TOO_MANY_EMAIL_AUTHENTICATION_REQUEST.status)