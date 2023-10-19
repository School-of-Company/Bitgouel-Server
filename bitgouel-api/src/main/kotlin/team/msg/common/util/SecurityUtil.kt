package team.msg.common.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Component
class SecurityUtil(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    fun passwordEncode(password: String): String =
        passwordEncoder.encode(password)

    /**
     * 현재 로그인 된 유저를 찾는 로직
     */
    fun queryCurrentUser(): User {
        val currentUserId = queryCurrentUserId()
        return userRepository.findByIdOrNull(currentUserId)
            ?: throw UserNotFoundException("로그인 된 유저를 찾을 수 없습니다. values : [ UUID = $currentUserId ]")
    }
    private fun queryCurrentUserId() =
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

}