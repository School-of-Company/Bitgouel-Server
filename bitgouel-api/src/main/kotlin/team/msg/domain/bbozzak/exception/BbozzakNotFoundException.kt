package team.msg.domain.bbozzak.exception

import team.msg.domain.bbozzak.exception.constant.BbozzakErrorCode
import team.msg.global.error.exception.BitgouelException

class BbozzakNotFoundException(
    message: String
) : BitgouelException(message, BbozzakErrorCode.BBOZZAK_NOT_FOUND.status)