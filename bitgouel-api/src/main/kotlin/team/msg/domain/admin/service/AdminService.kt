package team.msg.domain.admin.service

import org.springframework.data.domain.Pageable
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.presentation.data.response.UsersResponse

interface AdminService {
    fun queryUsers(request: QueryUsersRequest,pageable: Pageable): UsersResponse
}