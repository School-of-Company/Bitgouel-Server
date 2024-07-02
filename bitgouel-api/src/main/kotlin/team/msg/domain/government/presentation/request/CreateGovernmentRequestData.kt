package team.msg.domain.government.presentation.request

import team.msg.common.enums.Field

data class CreateGovernmentRequestData(
    val field: Field,
    val governmentName: String
)