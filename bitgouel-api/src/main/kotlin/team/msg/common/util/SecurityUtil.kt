package team.msg.common.util

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityUtil(
    private val passwordEncoder: PasswordEncoder
) {
    fun passwordEncode(password: String): String =
        passwordEncoder.encode(password)

    fun isPasswordMatch(currentPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(currentPassword, encodedPassword)
}