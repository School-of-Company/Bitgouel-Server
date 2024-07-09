package team.msg.domain.company.presentation.data.request

data class CreateCompanyRequest(
    val id: Long,
    val companyName: String,
    val field: String
)