package team.msg.domain.admin.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import org.springframework.context.ApplicationEventPublisher
import team.msg.domain.user.repository.UserRepository

class AdminServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userRepository = mockk<UserRepository>()
    val applicationEventPublisher = mockk<ApplicationEventPublisher>()
    val adminServiceImpl = AdminServiceImpl(
        userRepository = userRepository,
        applicationEventPublisher = applicationEventPublisher
    )
})