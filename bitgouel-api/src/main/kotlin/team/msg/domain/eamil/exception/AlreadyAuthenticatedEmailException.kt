package team.msg.domain.eamil.exception

import team.msg.domain.eamil.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyAuthenticatedEmailException(
    message: String
) : BitgouelException(message, EmailErrorCode.ALREADY_AUTHENTICATED_EMAIL.status)