package team.msg.domain.admin.exception

import team.msg.domain.admin.exception.constant.AdminErrorCode
import team.msg.global.error.exception.BitgouelException

class AlreadyExistStudentInformationException(
    message: String
) : BitgouelException(message, AdminErrorCode.ALREADY_EXIST_STUDENT_INFORMATION.status)