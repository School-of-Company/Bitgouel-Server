package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidRefreshTokenException(
    message: String
) : BitgouelException(message, AuthErrorCode.INVALID_TOKEN.status)