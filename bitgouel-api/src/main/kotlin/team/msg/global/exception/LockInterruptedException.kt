package team.msg.global.exception

import team.msg.global.error.GlobalErrorCode
import team.msg.global.error.exception.BitgouelException

class LockInterruptedException(
    msg: String
) : BitgouelException(msg, GlobalErrorCode.LOCK_INTERRUPTED.status)