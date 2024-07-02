package team.msg.domain.government.presentation.web

import javax.validation.constraints.NotBlank

data class CreateGovernmentWebRequest (
    @field:NotBlank
    val field: String,

    @field:NotBlank
    val governmentName: String
)