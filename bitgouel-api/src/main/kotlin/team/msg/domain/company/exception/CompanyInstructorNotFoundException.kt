package team.msg.domain.company.exception

import team.msg.domain.company.exception.constant.CompanyErrorCode
import team.msg.global.error.exception.BitgouelException

class CompanyInstructorNotFoundException(
    message: String
) : BitgouelException(message, CompanyErrorCode.COMPANY_INSTRUCTOR_NOT_FOUND.status)