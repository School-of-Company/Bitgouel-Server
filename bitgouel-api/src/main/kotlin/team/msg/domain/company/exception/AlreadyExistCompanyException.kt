package team.msg.domain.company.exception

import team.msg.domain.company.exception.constant.CompanyErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyExistCompanyException(
    message: String
) : BitgouelException(message, CompanyErrorCode.ALREADY_EXIST_COMPANY.status)