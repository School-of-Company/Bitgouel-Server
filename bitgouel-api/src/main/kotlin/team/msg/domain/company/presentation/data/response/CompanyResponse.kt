package team.msg.domain.company.presentation.data.response

import team.msg.common.enums.Field
import team.msg.domain.company.model.Company

data class CompanyResponse(
    val id: Long,
    val companyName: String,
    val field: Field
) {
    companion object {
        fun of(company: Company) = CompanyResponse(
            id = company.id,
            companyName = company.name,
            field = company.field
        )
    }
}

data class CompaniesResponse(
    val companies: List<CompanyResponse>
)