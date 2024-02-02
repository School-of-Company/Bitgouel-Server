package team.msg.domain.faq.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import team.msg.common.util.UserUtil
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.faq.repository.FaqRepository

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

})