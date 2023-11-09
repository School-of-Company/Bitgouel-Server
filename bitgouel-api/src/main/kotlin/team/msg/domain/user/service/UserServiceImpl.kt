package team.msg.domain.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.presentation.data.response.UserResponse

@Service
class UserServiceImpl(
    private val userUtil: UserUtil
) : UserService {
    /**
     * 현재 로그인한 유저의 마이페이지를 조회하는 비지니스 로직입니다.
     * @return 조회한 마이페이지 정보가 담긴 dto
     */
    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun queryUserPageService(): UserPageResponse {
        val user = userUtil.queryCurrentUser()
        val organization = userUtil.getAuthorityEntityAndOrganization(user).second

        return UserResponse.pageOf(user, organization)
    }
}