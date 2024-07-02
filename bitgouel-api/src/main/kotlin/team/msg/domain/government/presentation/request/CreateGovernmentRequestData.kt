package team.msg.domain.government.presentation.request

import javax.validation.constraints.NotBlank

data class CreateGovernmentRequestData (
    val field: String,
    val governmentName: String
)