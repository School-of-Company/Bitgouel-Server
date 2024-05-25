package team.msg.common.util

import com.google.common.hash.Hashing
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import team.msg.domain.auth.exception.MisMatchPasswordException
import team.msg.domain.user.exception.InvalidPasswordException
import team.msg.global.security.aes.AesProperties
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Component
class SecurityUtil(
    private val passwordEncoder: PasswordEncoder,
    private val aesProperties: AesProperties
) {

    fun passwordEncode(password: String): String =
        passwordEncoder.encode(password)

    fun isPasswordMatch(currentPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(currentPassword, encodedPassword)

    fun isValidPassword(password: String): Boolean =
        password.matches("^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\\\\\\\$%^&*]{8,24}\\\$".toRegex())

    fun decrypt(encryptedString: String): String =
        try {
            val secretKey = Arrays.copyOfRange(Hashing.sha1().hashString(aesProperties.secretKey, StandardCharsets.UTF_8).asBytes(), 0, 16)

            val hexedIv = encryptedString.substring(0, 32)
            val hexedCipherText = encryptedString.substring(32)

            val iv = DatatypeConverter.parseHexBinary(hexedIv)
            val cipherText = DatatypeConverter.parseHexBinary(hexedCipherText)

            val cipher = Cipher.getInstance(AesProperties.pkcs5Padding)
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secretKey, AesProperties.aes), IvParameterSpec(iv))

            String(cipher.doFinal(cipherText), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw MisMatchPasswordException("잘못된 방식으로 비밀번호를 암호화 했습니다.")
        }

}