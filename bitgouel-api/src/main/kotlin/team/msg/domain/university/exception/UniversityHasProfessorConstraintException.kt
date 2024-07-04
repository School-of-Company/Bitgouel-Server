package team.msg.domain.university.exception

import team.msg.domain.university.exception.constant.UniversityErrorCode
import team.msg.global.error.exception.BitgouelException

class UniversityHasProfessorConstraintException(
    message: String
) : BitgouelException(message, UniversityErrorCode.UNIVERSITY_HAS_PROFESSOR_CONSTRAINT.status) {
}