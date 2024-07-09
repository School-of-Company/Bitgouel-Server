package team.msg.domain.company.presentation.data.request

import team.msg.common.enums.Field

data class CreateCompanyRequest(
    val id: Long,
    val companyName: String,
    val field: Field
)