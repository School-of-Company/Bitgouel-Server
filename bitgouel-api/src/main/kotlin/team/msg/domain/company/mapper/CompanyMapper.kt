package team.msg.domain.company.mapper

import team.msg.domain.company.presentation.data.request.CreateCompanyRequest
import team.msg.domain.company.presentation.web.CreateCompanyWebRequest

interface CompanyMapper {
    fun createCompanyWebRequestToDto(webRequest: CreateCompanyWebRequest): CreateCompanyRequest
}