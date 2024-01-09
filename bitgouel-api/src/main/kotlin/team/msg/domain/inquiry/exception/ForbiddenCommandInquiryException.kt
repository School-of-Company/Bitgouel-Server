package team.msg.domain.inquiry.exception

import team.msg.domain.inquiry.exception.constant.InquiryErrorCode
import team.msg.global.error.exception.BitgouelException

class ForbiddenCommandInquiryException(message: String) : BitgouelException(message, InquiryErrorCode.FORBIDDEN_INQUIRY.status)