package team.msg.domain.admin.exception

import team.msg.domain.admin.exception.constant.AdminErrorCode
import team.msg.global.error.exception.BitgouelException

class InvalidCellTypeException(
    message: String
) : BitgouelException(message, AdminErrorCode.INVALID_CELL_TYPE.status)