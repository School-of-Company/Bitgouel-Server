package team.msg.domain.inquiry.exception

import team.msg.domain.inquiry.exception.constant.InquiryErrorCode
import team.msg.global.error.exception.BitgouelException

class InquiryNotFoundException(message: String) : BitgouelException(message, InquiryErrorCode.NOT_FOUND_INQUIRY.status)