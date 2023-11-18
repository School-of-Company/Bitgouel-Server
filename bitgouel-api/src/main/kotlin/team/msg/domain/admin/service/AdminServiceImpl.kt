package team.msg.domain.admin.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import team.msg.domain.user.repository.UserRepository

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository
) : AdminService {
    override fun queryUsers(keyword: String, pageable: Pageable): UsersResponse {
        val users =
            if(keyword == "")
                userRepository.findAll(pageable)
            else
                userRepository.findByNameContaining(keyword, pageable)

        return UserResponse.pageOf(users)
    }
}