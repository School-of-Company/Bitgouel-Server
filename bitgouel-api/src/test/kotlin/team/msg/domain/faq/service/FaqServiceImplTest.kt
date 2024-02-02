package team.msg.domain.faq.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.model.Admin
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.faq.model.Faq
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
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
    Given("CreateFaqRequest 가 주어지면") {
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

            Then("AdminNotFoundException 가 터져야 한다.") {
                shouldThrow<AdminNotFoundException> {
                    faqServiceImpl.createFaq(request)
                }
            }
        }
    }

})