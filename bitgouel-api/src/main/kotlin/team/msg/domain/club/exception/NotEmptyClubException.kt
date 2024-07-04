package team.msg.domain.club.exception

import team.msg.domain.club.exception.constant.ClubErrorCode
import team.msg.global.error.exception.BitgouelException

class NotEmptyClubException(
    message: String
) : BitgouelException(message, ClubErrorCode.NOT_EMPTY_CLUB.status)