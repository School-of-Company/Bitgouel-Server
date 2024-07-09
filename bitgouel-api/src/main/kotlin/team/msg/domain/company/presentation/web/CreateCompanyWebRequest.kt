package team.msg.domain.company.presentation.web

import javax.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class CreateCompanyWebRequest(
    @field:NotNull
    val id: Long,

    @field:NotBlank
    val companyName: String,

    @field:NotBlank
    val field: String
)