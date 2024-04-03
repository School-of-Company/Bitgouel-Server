package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class LockInterruptedException(
    message: String
) : BitgouelException(message, GlobalErrorCode.LOCK_INTERRUPTED.status)