package team.msg.domain.map.exception

import team.msg.domain.map.exception.constant.MapErrorCode
import team.msg.global.error.exception.BitgouelException

class AddressNotFoundException(
    message: String
) : BitgouelException(message, MapErrorCode.ADDRESS_NOT_FOUND.status)