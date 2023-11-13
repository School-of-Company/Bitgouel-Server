package team.msg.domain.certification.exception

import team.msg.domain.certification.exception.constant.CertificationErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenCertificationException(
    message: String
) : BitgouelException(message, CertificationErrorCode.FORBIDDEN_CERTIFICATION.status)