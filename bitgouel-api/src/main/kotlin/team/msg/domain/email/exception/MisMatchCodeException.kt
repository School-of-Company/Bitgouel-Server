package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class MisMatchCodeException(
    message: String
) : BitgouelException(message, EmailErrorCode.MIS_MATCH_CODE.status)