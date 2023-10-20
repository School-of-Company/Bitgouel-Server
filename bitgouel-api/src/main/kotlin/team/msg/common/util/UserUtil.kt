package team.msg.common.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.global.security.principal.AuthDetails
import java.util.*

@Component
class UserUtil(
    private val userRepository: UserRepository
) {
    fun queryCurrentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal

        val userId = UUID.fromString(if(principal is AuthDetails)
            principal.username
        else
            principal.toString())

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("존재하지 않는 유저입니다.")

        return user
    }
}