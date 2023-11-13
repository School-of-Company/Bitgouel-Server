package team.msg.domain.certifiacation.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.certifiacation.model.Certification
import java.util.UUID

interface CertificationRepository : CrudRepository<Certification, UUID> {
}