package team.msg.common.util

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.msg.global.security.aes.AesProperties

@Component
class SecurityUtil(
    private val passwordEncoder: PasswordEncoder,
    private val aesProperties: AesProperties
) {

    fun passwordEncode(password: String): String =
        passwordEncoder.encode(password)

    fun isPasswordMatch(currentPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(currentPassword, encodedPassword)

}