package team.msg.domain.admin.exception

import team.msg.domain.admin.exception.constant.AdminErrorCode
import team.msg.global.error.exception.BitgouelException

class AdminNotFoundException(
    message: String
) : BitgouelException(message, AdminErrorCode.ADMIN_NOT_FOUND.status)