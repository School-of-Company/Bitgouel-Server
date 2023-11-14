package team.msg.domain.certification.presentation.data.response

import team.msg.domain.certifiacation.model.Certification
import java.time.LocalDate
import java.util.UUID

data class CertificationResponse(
    val id: UUID,
    val name: String,
    val acquisitionDate: LocalDate
) {
    companion object {
        fun listOf(certifications: List<Certification>) = certifications.map {
            CertificationResponse(
                id = it.id,
                name = it.name,
                acquisitionDate = it.acquisitionDate
            )
        }
    }
}

data class AllCertificationsResponse(
    val certifications: List<CertificationResponse>
)