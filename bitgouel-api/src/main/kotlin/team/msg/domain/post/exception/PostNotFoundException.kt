package team.msg.domain.post.exception

import team.msg.domain.post.exception.constant.PostErrorCode
import team.msg.global.error.exception.BitgouelException

class PostNotFoundException(
    message: String
) : BitgouelException(message, PostErrorCode.POST_NOT_FOUND.status)