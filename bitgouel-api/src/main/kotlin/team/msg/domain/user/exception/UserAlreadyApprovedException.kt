package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class UserAlreadyApproved(
    message: String
) : BitgouelException(message, UserErrorCode.USER_ALREADY_APPROVED.status) {
}