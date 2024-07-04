package team.msg.domain.school.exception

import team.msg.domain.club.exception.constant.ClubErrorCode
import team.msg.domain.school.exception.constant.SchoolErrorCode
import team.msg.global.error.exception.BitgouelException

class NotEmptySchoolException(
    message: String
) : BitgouelException(message, SchoolErrorCode.NOT_EMPTY_SCHOOL.status)