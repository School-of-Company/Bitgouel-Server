package team.msg.global.security.principal

import team.msg.domain.user.exception.UserNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class], readOnly = true)
class AuthDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByIdOrNull(UUID.fromString(username)) ?: throw UserNotFoundException("존재하지 않는 유저입니다.")
        return AuthDetails(user.id)
    }
}