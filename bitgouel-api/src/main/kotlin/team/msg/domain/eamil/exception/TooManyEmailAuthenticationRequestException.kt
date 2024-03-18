package team.msg.domain.eamil.exception

import team.msg.domain.eamil.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class TooManyEmailAuthenticationRequestException(
    message: String
) : BitgouelException(message, EmailErrorCode.TOO_MANY_EMAIL_AUTHENTICATION_REQUEST.status)