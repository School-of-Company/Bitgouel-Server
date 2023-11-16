package team.msg.domain.certification.presentation.data.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.time.LocalDate

data class UpdateCertificationWebRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val acquisitionDate: LocalDate
)