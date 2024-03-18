package team.msg.domain.email.exception

import team.msg.domain.email.exception.constant.EmailErrorCode
import team.msg.global.error.exception.BitgouelException

class EmailSendFailException(
    message: String
) : BitgouelException(message, EmailErrorCode.EMAIL_SEND_FAIL.status)