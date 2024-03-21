package team.msg.domain.faq.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.model.Admin
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.faq.model.Faq
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.presentation.data.response.FaqResponse
import team.msg.domain.faq.presentation.data.response.FaqsResponse
import team.msg.domain.faq.repository.FaqRepository
import team.msg.domain.user.model.User

class FaqServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val faqRepository = mockk<FaqRepository>()
    val userUtil = mockk<UserUtil>()
    val adminRepository = mockk<AdminRepository>()
    val faqServiceImpl = FaqServiceImpl(
        faqRepository,
        userUtil,
        adminRepository
    )

    // createFaq 테스트 코드
    Given("CreateFaqRequest 가 주어질 때") {
        val user = fixture<User>()
        val admin = fixture<Admin>()
        val faq = fixture<Faq>()
        val request = fixture<CreateFaqRequest>()

        every { userUtil.queryCurrentUser() } returns user
        every { adminRepository.findByUser(user) } returns admin
        every { faqRepository.save(any()) } returns faq

        When("FAQ 등록 요청을 하면") {
            faqServiceImpl.createFaq(request)

            Then("FAQ 가 저장이 되어야 한다.") {
                verify(exactly = 1) { faqRepository.save(any()) }
            }
        }

        When("현재 유저가 어드민이 아니라면") {
            every { adminRepository.findByUser(user) } returns null

            Then("AdminNotFoundException이 발생해야 한다.") {
                shouldThrow<AdminNotFoundException> {
                    faqServiceImpl.createFaq(request)
                }
            }
        }
    }

    // queryAllFaqs 테스트 코드
    Given("FAQ 가 주어질 때") {
        val faqId = 1L
        val question = "question"
        val answer = "answer"
        val faq = fixture<Faq> {
            property(Faq::id) { faqId }
            property(Faq::question) { question }
            property(Faq::answer) { answer }
        }
        val faqResponse = fixture<FaqResponse> {
            property(FaqResponse::id) { faqId }
            property(FaqResponse::question) { question }
            property(FaqResponse::answer) { answer }
        }
        val response = fixture<FaqsResponse> {
            property(FaqsResponse::faqs) { listOf(faqResponse) }
        }

        every { faqRepository.findAll() } returns listOf(faq)

        When("FAQ 전체 조회 요청을 하면") {
            val result = faqServiceImpl.queryAllFaqs()

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }
    }

})