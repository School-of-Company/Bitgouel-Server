package bitgouel.team.msg.global.error.exception

import bitgouel.team.msg.global.error.ErrorCode

open class BitgouelException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)