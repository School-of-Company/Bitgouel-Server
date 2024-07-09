package team.msg.domain.company.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.company.exception.AlreadyExistCompanyException
import team.msg.domain.company.exception.CompanyHasCompanyInstructorConstraintException
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.model.Company
import team.msg.domain.company.presentation.data.request.CreateCompanyRequest
import team.msg.domain.company.presentation.data.response.CompaniesResponse
import team.msg.domain.company.presentation.data.response.CompanyResponse
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.company.repository.CompanyRepository

@Service
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository,
    private val companyInstructorRepository: CompanyInstructorRepository
) : CompanyService {

    /**
     * 기업을 생성하는 비지니스 로직입니다.
     * 같은 이름의 기업이 이미 존재하면 예외를 반환합니다.
     *
     * @param request 생성할 기업의 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createCompany(request: CreateCompanyRequest) {
        if(companyRepository.existsByName(request.companyName))
            throw AlreadyExistCompanyException("이미 존재하는 기업입니다. info : [ companyName = ${request.companyName} ]")

        val company = Company(
            name = request.companyName,
            field = request.field
        )

        companyRepository.save(company)
    }

    /**
     * 기업 리스트를 반환하는 비지니스 로직입니다.
     * @return 기업 리스트
     */
    @Transactional(readOnly = true)
    override fun queryCompanies(): CompaniesResponse {
        val companies = companyRepository.findAll()
        val response = CompaniesResponse(
            companies = companies
                .map {
                    CompanyResponse.of(it)
                }
        )

        return response
    }

    /**
     * 기업을 삭제하는 비지니스로직입니다.
     * @param id 삭제할 기업의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteCompany(id: Long) {
        val company = companyRepository.findByIdOrNull(id)
            ?: throw CompanyNotFoundException("존재하지 않는 기업입니다. info : [ companyId = $id ]")

        if(companyInstructorRepository.existsByCompany(company))
            throw CompanyHasCompanyInstructorConstraintException("아직 기업 강사가 존재하는 기업입니다. info : [ companyName = ${company.name} ]")

        companyRepository.delete(company)
    }
}