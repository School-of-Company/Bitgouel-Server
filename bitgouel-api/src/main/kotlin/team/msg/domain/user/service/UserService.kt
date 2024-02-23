package team.msg.domain.user.service

import team.msg.domain.user.presentation.data.request.ModifyPasswordRequest
import team.msg.domain.user.presentation.data.response.UserPageResponse

interface UserService {
    fun queryUserPage(): UserPageResponse
    fun modifyPassword(request: ModifyPasswordRequest)
}