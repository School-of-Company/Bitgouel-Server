package team.msg.domain.post.exception

import team.msg.domain.post.exception.constant.PostErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenPostException(
    message: String
) : BitgouelException(message, PostErrorCode.FORBIDDEN_POST.status)