package team.msg.domain.government.exception

import team.msg.domain.government.exception.constant.GovernmentErrorCode
import team.msg.global.error.exception.BitgouelException

class GovernmentHasGovernmentInstructorConstraintException(
    message: String
) : BitgouelException(message, GovernmentErrorCode.GOVERNMENT_HAS_GOVERNMENT_INSTRUCTOR_CONSTRAINT.status)