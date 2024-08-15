package team.msg.domain.admin.exception

import team.msg.domain.admin.exception.constant.AdminErrorCode
import team.msg.global.error.exception.BitgouelException

class EmptyCellException(
    message: String
) : BitgouelException(message, AdminErrorCode.EMPTY_CELL.status)