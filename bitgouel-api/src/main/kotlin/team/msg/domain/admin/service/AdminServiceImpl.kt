package team.msg.domain.admin.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import team.msg.domain.user.repository.UserRepository

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository
) : AdminService {
    /**
     * 유저를 전체 조회 및 이름으로 조회하는 비즈니스 로직입니다
     * @param 유저를 검색하기 위한 keyword 및 페이징을 처리하기 위한 pageable
     * @return 페이징된 학생 정보를 담은 Dto
     */
    override fun queryUsers(request: QueryUsersRequest,pageable: Pageable): UsersResponse {
        val users = userRepository.query(request.keyword, request.authority, pageable)

        return UserResponse.pageOf(users)
    }
}