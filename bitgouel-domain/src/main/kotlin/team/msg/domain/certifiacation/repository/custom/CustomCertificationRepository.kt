package team.msg.domain.certifiacation.repository.custom

import java.util.UUID

interface CustomCertificationRepository {
    fun deleteAllByStudentId(studentId: UUID)
}