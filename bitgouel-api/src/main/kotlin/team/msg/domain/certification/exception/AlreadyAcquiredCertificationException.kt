package team.msg.domain.certification.exception

import team.msg.domain.certification.exception.constant.CertificationErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyAcquiredCertificationException(
    message: String
) : BitgouelException(message, CertificationErrorCode.ALREADY_ACQUIRED_CERTIFICATION.status)