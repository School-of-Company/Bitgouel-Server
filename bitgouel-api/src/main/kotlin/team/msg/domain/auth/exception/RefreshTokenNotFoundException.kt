package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class RefreshTokenNotFoundException(
    message: String
) : BitgouelException(message, AuthErrorCode.REFRESH_TOKEN_NOT_FOUND.status)