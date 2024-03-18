package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyAuthenticatedEmailException(
    message: String
) : BitgouelException(message, EmailErrorCode.ALREADY_AUTHENTICATED_EMAIL.status)