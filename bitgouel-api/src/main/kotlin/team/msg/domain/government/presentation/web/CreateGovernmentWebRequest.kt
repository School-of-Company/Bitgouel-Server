package team.msg.domain.government.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field

data class CreateGovernmentWebRequest(
    @field:NotNull
    val field: Field,

    @field:NotBlank
    val governmentName: String
)