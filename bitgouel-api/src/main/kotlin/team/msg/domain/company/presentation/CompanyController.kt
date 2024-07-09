package team.msg.domain.company.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.company.mapper.CompanyMapper
import team.msg.domain.company.presentation.data.response.CompaniesResponse
import team.msg.domain.company.presentation.web.CreateCompanyWebRequest
import team.msg.domain.company.service.CompanyService

@RestController
@RequestMapping("/company")
class CompanyController(
    private val companyService: CompanyService,
    private val companyMapper: CompanyMapper
) {
    @PostMapping
    fun createCompany(webRequest: CreateCompanyWebRequest): ResponseEntity<Unit> {
        val request = companyMapper.createCompanyWebRequestToDto(webRequest)
        companyService.createCompany(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryCompanies(): ResponseEntity<CompaniesResponse> {
        val response = companyService.queryCompanies()
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteCompany(@PathVariable id: Long): ResponseEntity<Unit> {
        companyService.deleteCompany(id)
        return ResponseEntity.noContent().build()
    }
}