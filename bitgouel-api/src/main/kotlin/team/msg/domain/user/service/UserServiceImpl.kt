package team.msg.domain.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.exception.MisMatchPasswordException
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.request.ModifyPasswordRequest
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.domain.user.repository.UserRepository

@Service
class UserServiceImpl(
    private val userUtil: UserUtil,
    private val securityUtil: SecurityUtil,
    private val userRepository: UserRepository
) : UserService {
    /**
     * 현재 로그인한 유저의 마이페이지를 조회하는 비지니스 로직입니다.
     * @return 조회한 마이페이지 정보가 담긴 dto
     */
    @Transactional(readOnly = true)
    override fun queryUserPage(): UserPageResponse {
        val user = userUtil.queryCurrentUser()
        val organization = userUtil.getAuthorityEntityAndOrganization(user).second

        return UserResponse.listOf(user, organization)
    }

    /**
     * 현재 로그인한 유저의 비밀번호를 변경하는 비지니스 로직입니다.
     * @param 현재 비밀번호와 변경할 비밀번호 정보를 담은 dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun modifyPassword(request: ModifyPasswordRequest) {
        val user = userUtil.queryCurrentUser()

        if(!securityUtil.isPasswordMatch(request.currentPassword, user.password))
            throw MisMatchPasswordException("비밀번호가 일치하지 않습니다. info : [ password = ${request.currentPassword} ]")

        val encodedNewPassword = securityUtil.passwordEncode(request.newPassword)

        val modifiedPasswordUser = User(
            id = user.id,
            email = user.email,
            name = user.name,
            phoneNumber = user.phoneNumber,
            password = encodedNewPassword,
            authority = user.authority,
            approveStatus = user.approveStatus
        )

        userRepository.save(modifiedPasswordUser)
    }
}