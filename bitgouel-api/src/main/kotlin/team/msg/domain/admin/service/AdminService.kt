package team.msg.domain.admin.service

import org.springframework.data.domain.Pageable
import team.msg.domain.user.presentation.data.response.UsersResponse

interface AdminService {
    fun queryUsers(keyword: String, pageable: Pageable): UsersResponse
}