package team.msg.domain.user.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.entity.BaseUUIDEntity
import team.msg.common.enums.ApproveStatus
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.exception.SameAsOldPasswordException
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.MisMatchPasswordException
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.request.ModifyPasswordRequest
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.repository.UserRepository
import java.util.*

class UserServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val securityUtil = mockk<SecurityUtil>()
    val userUtil = mockk<UserUtil>()
    val userRepository = mockk<UserRepository>()

    val userServiceImpl = UserServiceImpl(
        securityUtil = securityUtil,
        userUtil = userUtil,
        userRepository = userRepository
    )

    // queryUserPage 테스트 코드
    Given("현재 유저가 로그인 되어있을 때") {
        val id = UUID.randomUUID()
        val name = "name"
        val email = "email"
        val phoneNumber = "01012345678"
        val authority = Authority.ROLE_ADMIN
        val organization = "교육청"

        val user = fixture<User> {
            property(User::id) { id }
            property(User::name) { name }
            property(User::email) { email }
            property(User::phoneNumber) { phoneNumber }
            property(User::authority) { authority }
        }

        val response = fixture<UserPageResponse> {
            property(UserPageResponse::id) { id }
            property(UserPageResponse::name) { name }
            property(UserPageResponse::email) { email }
            property(UserPageResponse::phoneNumber) {
                phoneNumber.run {
                    "${substring(0, 3)}-${substring(3, 7)}-${substring(7)}"
                }
            }
            property(UserPageResponse::authority) { authority }
            property(UserPageResponse::organization) { organization }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { userUtil.getAuthorityEntityAndOrganization(user) } returns Pair(fixture<BaseUUIDEntity>(), organization)

        When("마이페이지를 조회하면") {
            Then("result와 response가 같아야 한다.") {
                val result = userServiceImpl.queryUserPage()

                result shouldBe response
            }
        }
    }

    // modifyPassword 테스트 코드
    Given("ModifyPasswordRequest 가 주어졌을 때") {
        val currentPassword = "currentPassword"
        val newPassword = "newPassword"

        val modifyPasswordRequestA = fixture<ModifyPasswordRequest> {
            property(ModifyPasswordRequest::currentPassword) { currentPassword }
            property(ModifyPasswordRequest::newPassword) { newPassword }
        }
        val modifyPasswordRequestB = fixture<ModifyPasswordRequest> {
            property(ModifyPasswordRequest::currentPassword) { newPassword }
            property(ModifyPasswordRequest::newPassword) { newPassword }
        }
        val modifyPasswordRequestC = fixture<ModifyPasswordRequest> {
            property(ModifyPasswordRequest::currentPassword) { currentPassword }
            property(ModifyPasswordRequest::newPassword) { currentPassword }
        }

        val id = UUID.randomUUID()
        val name = "name"
        val email = "email"
        val phoneNumber = "01012345678"
        val authority = Authority.ROLE_ADMIN
        val encodedCurrentPassword = "encodedCurrentPassword"
        val encodedNewPassword = "encodedNewPassword"
        val approveStatus = ApproveStatus.APPROVED

        val user = fixture<User> {
            property(User::id) { id }
            property(User::name) { name }
            property(User::email) { email }
            property(User::phoneNumber) { phoneNumber }
            property(User::authority) { authority }
            property(User::password) { encodedCurrentPassword }
            property(User::approveStatus) { approveStatus }
        }
        val modifiedPasswordUser = fixture<User> {
            property(User::id) { id }
            property(User::name) { name }
            property(User::email) { email }
            property(User::phoneNumber) { phoneNumber }
            property(User::authority) { authority }
            property(User::password) { encodedNewPassword }
            property(User::approveStatus) { approveStatus }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { securityUtil.isPasswordMatch(currentPassword, encodedCurrentPassword) } returns true
        every { securityUtil.isPasswordMatch(newPassword, encodedCurrentPassword) } returns false
        every { securityUtil.passwordEncode(newPassword) } returns encodedNewPassword
        every { securityUtil.passwordEncode(currentPassword) } returns encodedCurrentPassword
        every { userRepository.save(user) } returns modifiedPasswordUser

        When("기존 비밀번호와 현재 유저의 비밀번호가 일치하지 않으면") {
            Then("MisMatchPasswordException 가 발생해야 한다") {
                shouldThrow<MisMatchPasswordException> {
                    userServiceImpl.modifyPassword(modifyPasswordRequestB)
                }
            }
        }

        When("새 비밀번호와 현재 유저의 비밀번호가 일치하면") {
            Then("SameAsOldPasswordException 가 발생해야 한다") {
                shouldThrow<SameAsOldPasswordException> {
                    userServiceImpl.modifyPassword(modifyPasswordRequestC)
                }
            }
        }

        When("비밀번호를 변경하면") {
            userServiceImpl.modifyPassword(modifyPasswordRequestA)

            Then("User 가 저장되어야 한다.") {
                verify(exactly = 1) { userRepository.save(user) }
            }
        }
    }

})
