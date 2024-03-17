package team.msg.domain.inquiry.repository.custom

import java.util.*

interface InquiryAnswerRepositoryCustom {
    fun deleteAllByAdminId(adminId: UUID)
}