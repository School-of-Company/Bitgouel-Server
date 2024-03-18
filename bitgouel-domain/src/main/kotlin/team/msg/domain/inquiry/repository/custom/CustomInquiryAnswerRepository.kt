package team.msg.domain.inquiry.repository.custom

import java.util.*

interface CustomInquiryAnswerRepository {
    fun deleteAllByAdminId(adminId: UUID)
}