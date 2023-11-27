package team.msg.domain.faq.exception

import team.msg.domain.faq.exception.constant.FaqErrorCode
import team.msg.global.error.exception.BitgouelException

class FaqNotFoundException(
    message: String
) : BitgouelException(message, FaqErrorCode.FAQ_NOT_FOUND.status)