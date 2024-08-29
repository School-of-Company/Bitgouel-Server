package team.msg.domain.club.exception

import team.msg.domain.club.exception.constant.ClubErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidFieldException(
    message: String
) : BitgouelException(message, ClubErrorCode.INVALID_FIELD.status)