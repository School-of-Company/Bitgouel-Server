package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class UnApprovedException(
    message: String
) : BitgouelException(message, AuthErrorCode.NOT_APPROVED.status)