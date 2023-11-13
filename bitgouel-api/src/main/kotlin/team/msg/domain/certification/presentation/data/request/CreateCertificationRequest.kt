package team.msg.domain.certification.presentation.data.request

import java.time.LocalDate

data class CreateCertificationRequest(
    val name: String,
    val acquisitionDate: LocalDate
)