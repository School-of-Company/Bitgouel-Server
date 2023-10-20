package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class MisMatchPasswordException(
    message: String
) : BitgouelException(message, AuthErrorCode.MISMATCH_PASSWORD.status)