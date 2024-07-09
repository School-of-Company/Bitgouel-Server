package team.msg.domain.company.exception

import team.msg.domain.company.exception.constant.CompanyErrorCode
import team.msg.global.error.exception.BitgouelException

class CompanyHasCompanyInstructorConstraintException(
    message: String
) : BitgouelException(message, CompanyErrorCode.COMPANY_HAS_COMPANY_INSTRUCTOR_CONSTRAINT.status)