package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class UnAuthenticatedEmailException(
    message: String
) : BitgouelException(message, EmailErrorCode.UNAUTHENTICATED_EMAIL.status)