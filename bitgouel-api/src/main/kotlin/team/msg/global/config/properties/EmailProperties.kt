package team.msg.global.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.mail")
class EmailProperties (
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val url: String


)