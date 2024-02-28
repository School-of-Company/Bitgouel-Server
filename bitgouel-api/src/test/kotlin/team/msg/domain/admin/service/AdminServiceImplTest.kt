package team.msg.domain.admin.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.ApplicationEventPublisher
import team.msg.common.enums.ApproveStatus
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.response.AdminUserResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import team.msg.domain.user.repository.UserRepository
import java.util.*

class AdminServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userRepository = mockk<UserRepository>()
    val applicationEventPublisher = mockk<ApplicationEventPublisher>()
    val adminServiceImpl = AdminServiceImpl(
        userRepository = userRepository,
        applicationEventPublisher = applicationEventPublisher
    )

    // queryUsers 테스트 코드
    Given("QueryUsersRequest가 주어졌을 때") {
        val userId = UUID.randomUUID()
        val keyword = "keyword"
        val authority = Authority.ROLE_STUDENT
        val approveStatus = ApproveStatus.APPROVED

        val user = fixture<User> {
            property(User::id) { userId }
            property(User::name) { keyword }
            property(User::authority) { authority }
            property(User::approveStatus) { approveStatus }

        }

        val request = fixture<QueryUsersRequest> {
            property(QueryUsersRequest::keyword) { keyword }
            property(QueryUsersRequest::authority) { authority }
            property(QueryUsersRequest::approveStatus) { approveStatus }
        }

        val adminUserResponse = fixture<AdminUserResponse> {
            property(AdminUserResponse::id) { userId }
            property(AdminUserResponse::name) { keyword }
            property(AdminUserResponse::authority) { authority }
            property(AdminUserResponse::approveStatus) { approveStatus }
        }

        val response = fixture<UsersResponse> {
            property(UsersResponse::users) { listOf(adminUserResponse) }
        }

        every { userRepository.query(keyword, authority, approveStatus) } returns listOf(user)
    }
})