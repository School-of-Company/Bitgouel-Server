package team.msg.domain.certification.presentation.data.request

import java.time.LocalDate

data class UpdateCertificationRequest(
    val name: String,
    val acquisitionDate: LocalDate
)