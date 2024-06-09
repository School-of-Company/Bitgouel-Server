package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class SameAsOldPasswordException(
    message: String
) : BitgouelException(message, AuthErrorCode.SAME_AS_OLD_PASSWORD.status)