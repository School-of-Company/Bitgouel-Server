package team.msg.domain.certifiacation.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.custom.CustomCertificationRepository
import java.util.UUID

interface CertificationRepository : CrudRepository<Certification, UUID>, CustomCertificationRepository {
    fun findAllByStudentIdOrderByAcquisitionDateDesc(studentId: UUID): List<Certification>
}