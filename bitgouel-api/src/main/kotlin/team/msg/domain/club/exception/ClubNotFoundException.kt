package team.msg.domain.club.exception

import team.msg.domain.club.exception.constant.ClubErrorCode
import team.msg.global.error.exception.BitgouelException

class ClubNotFoundException(
    message: String
) : BitgouelException(message, ClubErrorCode.CLUB_NOT_FOUND.status)