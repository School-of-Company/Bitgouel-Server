package team.msg.domain.inquiry.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.inquiry.model.Inquiry
import team.msg.domain.inquiry.presentation.request.CreateInquiryRequest
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
})