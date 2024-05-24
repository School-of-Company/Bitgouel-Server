package team.msg.common.util

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import sun.security.x509.CertificateAlgorithmId.ALGORITHM
import team.msg.global.security.aes.AesProperties
import java.nio.charset.StandardCharsets
import java.util.*


@Component
class SecurityUtil(
    private val passwordEncoder: PasswordEncoder,
    private val aesProperties: AesProperties
) {
    fun passwordEncode(password: String): String =
        passwordEncoder.encode(password)

    fun isPasswordMatch(currentPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(currentPassword, encodedPassword)


    fun bcrypt(encryptedText: String): String {
        val key = SecretKeySpec(aesProperties.secretKey.toByteArray(StandardCharsets.UTF_8), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)

        cipher.init(Cipher.DECRYPT_MODE, key)

        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(decodedBytes)

        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

}