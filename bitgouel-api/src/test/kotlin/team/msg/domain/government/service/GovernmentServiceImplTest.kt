package team.msg.domain.government.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.enums.Field
import team.msg.domain.government.exception.GovernmentNotFoundException
import team.msg.domain.government.exception.AlreadyExistGovernmentException
import team.msg.domain.government.model.Government
import team.msg.domain.government.presentation.request.CreateGovernmentRequestData
import team.msg.domain.government.presentation.response.GovernmentResponse
import team.msg.domain.government.repository.GovernmentRepository

class GovernmentServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val governmentRepository = mockk<GovernmentRepository>()
    val governmentServiceImpl = GovernmentServiceImpl(
        governmentRepository
    )

    // createGovernment 테스트 코드
    Given("CreateGovernmentRequest가 주어질 때") {

        val governmentName = "government"
        val field = Field.ENERGY
        val request = fixture<CreateGovernmentRequestData> {
            property(CreateGovernmentRequestData::governmentName) { governmentName }
            property(CreateGovernmentRequestData::field) { field }
        }

        val governmentId = 0L
        val government = fixture<Government> {
            property(Government::id) { governmentId }
            property(Government::name) { governmentName }
            property(Government::field) { field }
        }

        every { governmentRepository.existsByName(governmentName) } returns false
        every { governmentRepository.save(any()) } returns government

        When("유관기관 등록 요청을 하면"){
            governmentServiceImpl.createGovernment(request)

            Then("유관기관이 저장되어야 한다."){
                verify(exactly = 1) { governmentRepository.save(any()) }
            }
        }

        When("같은 이름의 유관기관이 이미 존재하면") {
            every { governmentRepository.existsByName(governmentName) } returns true

            Then("AlreadyExistGovernmentException이 발생해야 한다.") {
                shouldThrow<AlreadyExistGovernmentException> {
                    governmentServiceImpl.createGovernment(request)
                }
            }
        }
    }

    // queryGovernment 테스트 코드
    Given("유관기관들이 주어질 때") {
        val governmentAId = 0L
        val governmentAName = "government"
        val fieldA = Field.ENERGY
        val governmentA = fixture<Government> {
            property(Government::id) { governmentAId }
            property(Government::name) { governmentAName }
            property(Government::field) { fieldA }
        }

        val governmentBId = 1L
        val governmentBName = "government"
        val fieldB = Field.ENERGY
        val governmentB = fixture<Government> {
            property(Government::id) { governmentBId }
            property(Government::name) { governmentBName }
            property(Government::field) { fieldB }
        }

        val governments = listOf(governmentA,governmentB)
        val response = GovernmentResponse.listOf(governments)

        every { governmentRepository.findAll() } returns governments

        When("유관기관 리스트를 조회하면") {
            val result = governmentServiceImpl.queryGovernments()

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // deleteGovernment 테스트 코드
    Given("government id가 주어질 때") {

        val governmentId = 0L
        val governmentName = "government"
        val field = Field.ENERGY

        val government = fixture<Government> {
            property(Government::id) { governmentId }
            property(Government::name) { governmentName }
            property(Government::field) { field }
        }

        every { governmentRepository.findByIdOrNull(governmentId) } returns government
        every { governmentRepository.delete(any()) } returns Unit

        When("유관기관을 삭제하면") {
            governmentServiceImpl.deleteGovernment(governmentId)

            Then("유관기관이 삭제되어야 한다.") {
                verify(exactly = 1) { governmentRepository.delete(any()) }
            }
        }

        When("id에 따른 유관기관을 찾을 수 없다면") {
            every { governmentRepository.findByIdOrNull(governmentId) } returns null

            Then("GovernmentNotFoundException이 발생해야 한다.") {
                shouldThrow<GovernmentNotFoundException> {
                    governmentServiceImpl.deleteGovernment(governmentId)
                }
            }
        }
    }
})