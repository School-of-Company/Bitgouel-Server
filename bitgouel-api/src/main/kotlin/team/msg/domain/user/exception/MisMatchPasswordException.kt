package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class MisMatchPasswordException(
    message: String
) : BitgouelException(message, UserErrorCode.MISMATCH_PASSWORD.status)