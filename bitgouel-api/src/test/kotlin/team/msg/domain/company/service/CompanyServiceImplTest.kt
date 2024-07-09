package team.msg.domain.company.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.enums.Field
import team.msg.domain.company.exception.AlreadyExistCompanyException
import team.msg.domain.company.exception.CompanyHasCompanyInstructorConstraintException
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.model.Company
import team.msg.domain.company.presentation.data.request.CreateCompanyRequest
import team.msg.domain.company.presentation.data.response.CompanyResponse
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.company.repository.CompanyRepository


class CompanyServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val companyRepository = mockk<CompanyRepository>()
    val companyInstructorRepository = mockk<CompanyInstructorRepository>()
    val companyServiceImpl = CompanyServiceImpl(
        companyRepository,
        companyInstructorRepository
    )

    // createCompany 테스트 코드
    Given("CreateCompanyRequest가 주어질 때") {

        val companyName = "company"
        val field = Field.ENERGY
        val request = fixture<CreateCompanyRequest> {
            property(CreateCompanyRequest::companyName) { companyName }
            property(CreateCompanyRequest::field) { field }
        }

        val companyId = 0L
        val company = fixture<Company> {
            property(Company::id) { companyId }
            property(Company::name) { companyName }
            property(Company::field) { field }
        }

        every { companyRepository.existsByName(companyName) } returns false
        every { companyRepository.save(any()) } returns company

        When("기업 등록 요청을 하면"){
            companyServiceImpl.createCompany(request)

            Then("기업이 저장되어야 한다."){
                verify(exactly = 1) { companyRepository.save(any()) }
            }
        }

        When("같은 이름의 기업이 이미 존재하면") {
            every { companyRepository.existsByName(companyName) } returns true

            Then("AlreadyExistCompanyException이 발생해야 한다.") {
                shouldThrow<AlreadyExistCompanyException> {
                    companyServiceImpl.createCompany(request)
                }
            }
        }
    }

    // queryCompanies 테스트 코드
    Given("기업들이 주어질 때") {
        val companyAId = 0L
        val companyAName = "companyA"
        val fieldA = Field.ENERGY
        val companyA = fixture<Company> {
            property(Company::id) { companyAId }
            property(Company::name) { companyAName }
            property(Company::field) { fieldA }
        }

        val companyBId = 1L
        val companyBName = "companyB"
        val fieldB = Field.ENERGY
        val governmentB = fixture<Company> {
            property(Company::id) { companyBId }
            property(Company::name) { companyBName }
            property(Company::field) { fieldB }
        }

        val companies = listOf(companyA, governmentB)
        val response = CompanyResponse.listOf(companies)

        every { companyRepository.findAll() } returns companies

        When("기업 리스트를 조회하면") {
            val result = companyServiceImpl.queryCompanies()

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // deleteCompany 테스트 코드
    Given("company id가 주어질 때") {

        val companyId = 0L
        val companyName = "company"
        val field = Field.ENERGY

        val company = fixture<Company> {
            property(Company::id) { companyId }
            property(Company::name) { companyName }
            property(Company::field) { field }
        }

        every { companyRepository.findByIdOrNull(companyId) } returns company
        every { companyInstructorRepository.existsByCompany(company) } returns false
        every { companyRepository.delete(any()) } just runs

        When("기업을 삭제하면") {
            companyServiceImpl.deleteCompany(companyId)

            Then("기업이 삭제되어야 한다.") {
                verify(exactly = 1) { companyRepository.delete(any()) }
            }
        }

        When("id에 따른 기업을 찾을 수 없다면") {
            every { companyRepository.findByIdOrNull(companyId) } returns null

            Then("CompanyNotFoundException이 발생해야 한다.") {
                shouldThrow<CompanyNotFoundException> {
                    companyServiceImpl.deleteCompany(companyId)
                }
            }
        }

        When("기업 기업 강사가 아직 존재한다면") {
            every { companyInstructorRepository.existsByCompany(company) } returns true

            Then("CompanyHasCompanyInstructorConstraintException이 발생해야 한다.") {
                shouldThrow<CompanyHasCompanyInstructorConstraintException> {
                    companyServiceImpl.deleteCompany(companyId)
                }
            }
        }
    }
})