package bitgouel.team.msg.global.error.exception

import bitgouel.team.msg.global.error.GlobalErrorCode

open class BitgouelException(
    val globalErrorCode: GlobalErrorCode
) : RuntimeException(globalErrorCode.message)