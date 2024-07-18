package team.msg.thirdparty.feign.company.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import team.msg.domain.company.presentation.data.response.CompaniesResponse

@FeignClient(value = "company", url = "http://localhost:8080/company")
interface CompanyClient {

    @GetMapping
    fun queryCompanies(): CompaniesResponse

}