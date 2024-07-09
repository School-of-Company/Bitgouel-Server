package team.msg.domain.company.mapper

import org.springframework.stereotype.Component
import team.msg.domain.company.presentation.data.request.CreateCompanyRequest
import team.msg.domain.company.presentation.web.CreateCompanyWebRequest

@Component
class CompanyMapperImpl : CompanyMapper {
    override fun createCompanyWebRequestToDto(webRequest: CreateCompanyWebRequest) = CreateCompanyRequest(
        companyName = webRequest.companyName,
        field = webRequest.field
    )
}