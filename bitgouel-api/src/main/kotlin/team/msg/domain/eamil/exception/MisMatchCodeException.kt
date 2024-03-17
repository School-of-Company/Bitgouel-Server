package team.msg.domain.eamil.exception

import team.msg.domain.eamil.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class MisMatchCodeException(
    message: String
) : BitgouelException(message, EmailErrorCode.MIS_MATCH_CODE.status)