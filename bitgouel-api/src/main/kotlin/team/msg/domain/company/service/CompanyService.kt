package team.msg.domain.company.service

import team.msg.domain.company.presentation.data.request.CreateCompanyRequest
import team.msg.domain.company.presentation.data.response.CompaniesResponse

interface CompanyService {
    fun createCompany(request: CreateCompanyRequest)
    fun queryCompanies(): CompaniesResponse
    fun deleteCompany(id: Long)
}