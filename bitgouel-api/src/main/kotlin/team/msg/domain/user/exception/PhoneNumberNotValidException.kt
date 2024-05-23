package team.msg.domain.user.exception

import team.msg.domain.user.exception.constant.UserErrorCode
import team.msg.global.error.exception.BitgouelException

class PhoneNumberNotValidException(
    message: String
) : BitgouelException(message, UserErrorCode.PHONE_NUMBER_NOT_VALID.status)