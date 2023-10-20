package team.msg.domain.auth.exception

import team.msg.domain.auth.exception.constant.AuthErrorCode
import team.msg.global.error.exception.BitgouelException

class UnApprovedUserException(
    message: String
) : BitgouelException(message, AuthErrorCode.UNAPPROVED_USER.status)