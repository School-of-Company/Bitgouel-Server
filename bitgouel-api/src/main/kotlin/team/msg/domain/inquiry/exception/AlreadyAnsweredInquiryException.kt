package team.msg.domain.inquiry.exception

import team.msg.domain.inquiry.exception.constant.InquiryErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyAnsweredInquiryException(message: String) : BitgouelException(message, InquiryErrorCode.ALREADY_ANSWERED_INQUIRY.status)