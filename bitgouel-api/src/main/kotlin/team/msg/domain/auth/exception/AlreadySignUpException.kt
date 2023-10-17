package team.msg.domain.auth.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException


class AlreadySignUpException(
    message: String
) : BitgouelException(message, UserErrorCode.ALREADY_SIGN_UP.status)