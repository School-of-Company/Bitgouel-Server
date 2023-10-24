package team.msg.domain.company.exception

import team.msg.domain.company.exception.constant.CompanyErrorCode
import team.msg.global.error.exception.BitgouelException

class CompanyNotFoundException(
    message: String
) : BitgouelException(message, CompanyErrorCode.COMPANY_NOT_FOUND.status)