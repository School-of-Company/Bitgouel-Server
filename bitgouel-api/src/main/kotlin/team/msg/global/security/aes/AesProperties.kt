package team.msg.global.security.aes

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("aes.bcrypt")
class AesProperties(
    val secretKey: String
)