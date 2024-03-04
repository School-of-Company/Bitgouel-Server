package team.msg.domain.inquiry.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
import team.msg.domain.inquiry.presentation.response.InquiryResponse
import team.msg.domain.inquiry.presentation.response.InquiryResponses
import team.msg.domain.inquiry.repository.InquiryAnswerRepository
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.user.model.User

class InquiryServiceImplTest : BehaviorSpec ({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userUtil = mockk<UserUtil>()
    val inquiryRepository = mockk<InquiryRepository>()
    val inquiryAnswerRepository = mockk<InquiryAnswerRepository>()
    val inquiryServiceImpl = InquiryServiceImpl(
        userUtil,
        inquiryRepository,
        inquiryAnswerRepository
    )

    // createInquiry 테스트 코드
    Given("CreateInquiryRequest 가 주어질 때") {
        val user = fixture<User>()
        val inquiry = fixture<Inquiry>()
        val request = fixture<CreateInquiryRequest>()

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.save(any()) } returns inquiry

        When("문의사항 등록 요청을 하면") {
            inquiryServiceImpl.createInquiry(request)

            Then("Inquiry 이 저장이 되어야 한다.") {
                verify(exactly = 1) { inquiryRepository.save(any()) }
            }
        }
    }

    // queryMyInquiries 테스트 코드
    Given("Inquiry 가 주어질 때") {
        val user = fixture<User>()
        val inquiry = fixture<Inquiry>()
        val inquiryResponse = fixture<InquiryResponse> {
            property(InquiryResponse::id) { inquiry.id }
            property(InquiryResponse::question) { inquiry.question }
            property(InquiryResponse::userId) { inquiry.user.id }
            property(InquiryResponse::username) { inquiry.user.name }
            property(InquiryResponse::createdAt) { inquiry.createdAt.toLocalDate() }
            property(InquiryResponse::answerStatus) { inquiry.answerStatus }
        }
        val response = fixture<InquiryResponses> {
            property(InquiryResponses::inquiries) { listOf(inquiryResponse) }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { inquiryRepository.findAllByUser(user) } returns listOf(inquiry)

        When("자격증 전체 조회 요청을 하면") {
            val result = inquiryServiceImpl.queryMyInquiries()

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }
    }
})