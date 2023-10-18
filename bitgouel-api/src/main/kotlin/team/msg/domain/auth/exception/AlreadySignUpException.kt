package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException


class AlreadySignUpException(
    message: String
) : BitgouelException(message, AuthErrorCode.ALREADY_SIGN_UP.status)