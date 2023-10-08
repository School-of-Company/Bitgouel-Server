package bitgouel.team.msg.global.error.exception

import bitgouel.team.msg.global.error.ErrorCode

class InternalServerException : BitgouelException(ErrorCode.INTERNAL_SERVER_ERROR)