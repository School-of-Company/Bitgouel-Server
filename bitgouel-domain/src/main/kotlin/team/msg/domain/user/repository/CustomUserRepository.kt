package team.msg.domain.user.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User

interface CustomUserRepository {
    fun query(keyword: String, authority: Authority, pageable: Pageable): Page<User>
}