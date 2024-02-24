package team.msg.domain.admin.service

import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.presentation.data.response.UserDetailsResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import java.util.*

interface AdminService {
    fun queryUsers(request: QueryUsersRequest): UsersResponse
    fun approveUsers(userIds: List<UUID>)
    fun rejectUser(userId: UUID)
    fun queryUserDetails(userId: UUID): UserDetailsResponse
    fun forceWithdraw(userId: UUID)
}