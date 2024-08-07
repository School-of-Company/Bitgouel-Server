package team.msg.domain.company.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field

data class CreateCompanyWebRequest(
    @field:NotBlank
    val companyName: String,

    @field:NotNull
    val field: Field
)